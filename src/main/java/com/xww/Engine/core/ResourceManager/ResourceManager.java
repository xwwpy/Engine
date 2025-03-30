package com.xww.Engine.core.ResourceManager;

import com.xww.Engine.Utils.ImgUtils;
import com.xww.Engine.Utils.StringUtils;
import com.xww.Engine.core.Animation.Atlas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ResourceManager {

    private static final ResourceManager instance = new ResourceManager();
    private ResourceManager() {}

    public final Map<String, Atlas> atlasPool = new HashMap<>();
    public final Map<String, BufferedImage> imagePool = new HashMap<>();

    public final Map<String, String> audioPathPool = new HashMap<>();

    public static ResourceManager getInstance() {
        return instance;
    }

    public Atlas findAtlas(String name) {
        Atlas atlas = atlasPool.get(name);
        if (atlas == null) {
            System.out.println("atlas " + name + " is null");
        }
        return atlas;
    }

    public BufferedImage findImage(String name) {
        BufferedImage image = imagePool.get(name);
        if (image == null) {
            System.out.println("image " + name + " is null");
        }
        return image;
    }

    public void loadAtlas(String name, String path_template, int num, int start) {
        Atlas atlas = new Atlas();
        atlas.load(path_template, num, start);
        atlasPool.put(name, atlas);
    }

    public void loadImage(String name, String path) {
        BufferedImage image = ImgUtils.loadImage(path);
        imagePool.put(name, image);
    }

    /**
     * 遍历指定文件夹的所有资源 以文件名作为key 存储到imagePool中
     */
    public void loadAll(String path, boolean prefixFlag, String... suffix) {
        try (Stream<Path> stream = Files.walk(new File(path).toPath(), FileVisitOption.FOLLOW_LINKS)) {
            stream.forEach(filePath -> {
                for (String fileSuffix : suffix) {
                    if (filePath.toString().endsWith(fileSuffix)) {
                        processFileWithSuffix(filePath, fileSuffix, prefixFlag);
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("遍历文件夹错误: " + path);
        }
    }

    /**
     * 加载所有图集 默认名称为该图集的上层文件夹名称 如果含有图集的文件夹中还有子文件夹 则名称为上层文件夹名称加上子文件夹名称
     * @param template 图集名称的模版不包含后缀 目前仅支持%d
     * @param prefix    图集名称的前缀用于得到图集的存储的名称 如prefix为null 则为文件夹名称 如prefix为"enemy" 则为enemy_文件夹名称
     * @param suffix    支持的文件的后缀名
     *
     */
    public void loadAllAtlas(String path, String template, String prefix, String... suffix) {
        File file = new File(path);
        // 得到图集资源的名称
        String atlasName;
        if (prefix != null) {
             atlasName = prefix + "_" + StringUtils.getDictionaryName(file.toPath());
        } else {
            atlasName = StringUtils.getDictionaryName(file.toPath());
        }
        // 得到文件夹下的所有文件或文件夹
        Atlas atlas = new Atlas();
        StringBuilder pathTemplate = new StringBuilder(path);
        // 此时的pathTemplate 不包括后缀名
        if (path.endsWith("/")){
            pathTemplate.append(template);
        } else {
            pathTemplate.append("/").append(template);
        }
        // 处理该文件夹中的所有文件
        try(DirectoryStream<Path> stream =  Files.newDirectoryStream(file.toPath())) {
            stream.forEach((filePath) -> {
                if (filePath.toFile().isDirectory()){
                    // 递归处理文件夹
                    loadAllAtlas(filePath.toString(), template, atlasName, suffix);
                } else {
                    // 处理文件
                    Arrays.stream(suffix).forEach((suf)->{
                        // 判断文件是否符合指定包含的后缀
                        if (filePath.toString().endsWith(suf)){
                            // 判断文件是否符合模版
                            int index = StringUtils.suitTemplate(filePath.toString(), pathTemplate + suf);
                            if (index != -1){
                                // 符合图集的模版时则加载该文件到图集中
                                atlas.loadSingle(filePath.toString(), index - 1);
                            }
                        }
                    });
                }
            });
            if (atlas.getSize() > 0) {
                atlasPool.put(atlasName, atlas);
                // 为了实现根据序号有序排列
                atlas.clearNull();
            }
        } catch (IOException e) {
            throw new RuntimeException("解析文件夹的图集失败");
        }
    }
    /**
     * 加载所有图集 默认名称为该图集的上层文件夹名称 如果含有图集的文件夹中还有子文件夹 则名称为上层文件夹名称加上子文件夹名称
     * @param template 图集名称的模版不包含后缀 目前仅支持%d
     * @param startIndex 图集的起始序号
     * @param prefix    图集名称的前缀用于得到图集的存储的名称 如prefix为null 则为文件夹名称 如prefix为"enemy" 则为enemy_文件夹名称
     * @param suffix    支持的文件的后缀名
     *
     */
    public void loadAllAtlas(String path, String template, int startIndex, String prefix, String... suffix) {
        File file = new File(path);
        // 得到图集资源的名称
        String atlasName;
        if (prefix != null) {
            atlasName = prefix + "_" + StringUtils.getDictionaryName(file.toPath());
        } else {
            atlasName = StringUtils.getDictionaryName(file.toPath());
        }
        // 得到文件夹下的所有文件或文件夹
        Atlas atlas = new Atlas();
        StringBuilder pathTemplate = new StringBuilder(path);
        // 此时的pathTemplate 不包括后缀名
        if (path.endsWith("/")){
            pathTemplate.append(template);
        } else {
            pathTemplate.append("/").append(template);
        }
        // 处理该文件夹中的所有文件
        try(DirectoryStream<Path> stream =  Files.newDirectoryStream(file.toPath())) {
            stream.forEach((filePath) -> {
                if (filePath.toFile().isDirectory()){
                    // 递归处理文件夹
                    loadAllAtlas(filePath.toString(), template, startIndex, atlasName, suffix);
                } else {
                    // 处理文件
                    Arrays.stream(suffix).forEach((suf)->{
                        // 判断文件是否符合指定包含的后缀
                        if (filePath.toString().endsWith(suf)){
                            // 判断文件是否符合模版
                            int index = StringUtils.suitTemplate(filePath.toString(), pathTemplate + suf);
                            if (index != -1){
                                // 符合图集的模版时则加载该文件到图集中
                                atlas.loadSingle(filePath.toString(), index - startIndex);
                            }
                        }
                    });
                }
            });
            if (atlas.getSize() > 0) {
                atlasPool.put(atlasName, atlas);
                // 为了实现根据序号有序排列
                atlas.clearNull();
            }
        } catch (IOException e) {
            throw new RuntimeException("解析文件夹的图集失败");
        }
    }

    /**
     * 处理带有指定后缀的文件
     * @param filePath 文件的路径
     * @param fileSuffix 文件的后缀
     * @param prefixFlag 是否需要添加前缀 用于getResourceName函数的参数
     */
    private void processFileWithSuffix(java.nio.file.Path filePath, String fileSuffix, boolean prefixFlag) {
        if (fileSuffix.equals(".png")) {
            loadImage(getResourceName(filePath, fileSuffix, prefixFlag), filePath.toString());
        }
    }

    /**
     * 根据文件的路径和后缀名 得到资源名称
     * @param filePath 文件的路径
     * @param fileSuffix 文件的后缀
     * @param prefixFlag 当为false 时 返回文件名 为true时 返回 文件夹名称 + _ + 文件名
     */
    private static String getResourceName(Path filePath, String fileSuffix, boolean prefixFlag) {
        if (!prefixFlag) {
            return StringUtils.removeSuffix(filePath.getFileName().toString(), fileSuffix);
        } else {
            return StringUtils.getDictionaryName(filePath) + "_" + StringUtils.removeSuffix(filePath.getFileName().toString(), fileSuffix);
        }
    }

    /**
     *
     * @param name 指定进行翻转的资源名称
     * @param connectStr 连接符
     * @param targetNameSuffix 反转后的资源名称的后缀 二者之间使用 connectStr 连接
     */
    public void flipImage(String name, String connectStr,String targetNameSuffix, Map<String, BufferedImage> imagePool) {
        BufferedImage image = instance.imagePool.get(name);
        if (image == null){
            throw new RuntimeException("image " + name + " is null, 不可以进行翻转");
        }
        if (imagePool == null) instance.imagePool.put(name + connectStr + targetNameSuffix, ImgUtils.convertToBufferedImage(ImgUtils.flipImage(image)));
        else imagePool.put(name + connectStr + targetNameSuffix, ImgUtils.convertToBufferedImage(ImgUtils.flipImage(image)));
    }
    /**
     *
     * @param name 指定进行翻转的资源名称
     * @param connectStr 连接符
     * @param targetNameSuffix 反转后的资源名称的后缀 二者之间使用 connectStr 连接
     */
    public void flipAtlas(String name, String connectStr,String targetNameSuffix, Map<String, Atlas> atlasPool) {
        Atlas atlas = instance.atlasPool.get(name);
        if (atlas == null){
            throw new RuntimeException("atlas " + name + " is null, 不可以进行翻转");
        }
        Atlas flipped = new Atlas();
        for (int i = 0; i < atlas.getSize(); i++) {
            flipped.addImage(ImgUtils.convertToBufferedImage(ImgUtils.flipImage(atlas.getImage(i))));
        }
        if (atlasPool == null) instance.atlasPool.put(name + connectStr + targetNameSuffix, flipped);
        else atlasPool.put(name + connectStr + targetNameSuffix, flipped);
    }

    /**
     * 加载音频资源
     * @param dictionaryPath 音频文件夹的路径
     */
    public void loadAllAudio(String dictionaryPath, String... suffix) {
        File file = new File(dictionaryPath);
        try(DirectoryStream<Path> stream =  Files.newDirectoryStream(file.toPath())) {
            stream.forEach((filePath) -> {
                if (filePath.toFile().isDirectory()){
                    // 递归处理文件夹
                    loadAllAudio(filePath.toString());
                } else {
                    // 处理文件
                    Arrays.stream(suffix).forEach((suf)->{
                        // 判断文件是否符合指定包含的后缀
                        if (filePath.toString().endsWith(suf)){
                            // 加载音频
                            audioPathPool.put(StringUtils.removeSuffix(String.valueOf(filePath.getFileName()), suf), filePath.toString());
                        }
                    });
                }
            });
        } catch (IOException e){
            throw new RuntimeException("解析文件夹的音频失败");
        }
    }

    public String findAudioPath(String bgm) {
        if (audioPathPool.containsKey(bgm)){
            return audioPathPool.get(bgm);
        } else{
            System.out.println("找不到音频：" + bgm);
        }
        return null;
    }
}
