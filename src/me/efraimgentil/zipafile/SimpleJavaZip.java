package me.efraimgentil.zipafile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SimpleJavaZip {

    public static void main(String[] args) throws IOException, URISyntaxException {
        zippingASingleFile();
        zippingAFolder();
    }

    public static void zippingASingleFile() throws IOException, URISyntaxException{
        FileOutputStream fileOutputStream = new FileOutputStream("myZip.zip"); // zip file destination
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        zipOutputStream.putNextEntry(new ZipEntry("sometextfile.txt")); // name of the file inside the zip
        Path fileToZip = Paths.get(SimpleJavaZip.class.getResource("/resources/sometextfile.txt").toURI());
        zipOutputStream.write(Files.readAllBytes(fileToZip));
        zipOutputStream.closeEntry();
        zipOutputStream.close();
    }

    public static void zippingAFolder() throws IOException, URISyntaxException{
        FileOutputStream fileOutputStream = new FileOutputStream("zippingAFolder.zip");
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

        Path rootPath = Paths.get(SimpleJavaZip.class.getResource("/resources").toURI());
        Files.walkFileTree( rootPath ,new SimpleFileVisitor<>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                zipOutputStream.putNextEntry(buildEntry(rootPath, file));
                zipOutputStream.write(Files.readAllBytes(file));
                zipOutputStream.closeEntry();
                return FileVisitResult.CONTINUE;
            }
        });
        zipOutputStream.close();
    }

    public static ZipEntry buildEntry(Path rootPath, Path file){
        return new ZipEntry(file.toString().replace(rootPath.toString() + File.separator, ""));
    }
}
