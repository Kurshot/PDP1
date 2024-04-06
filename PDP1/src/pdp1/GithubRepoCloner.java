/**
*
* @author Ahmet Kürşat Sonkur - ahmet.sonkur@ogr.sakarya.edu.tr - b211210010
* @since 04/04/2024
* <p>
* Github'dan repo çekme işlemleri, .java uzantılı dosyaların ayıklanıp listeye atanması.
* </p>
*/
package pdp1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GithubRepoCloner {
    
    private List<String> javaFiles;
    
    // Constructor
    public GithubRepoCloner() {
        cloneRepository();
    }
    
    public void cloneRepository() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("GitHub repo URL'sini girin: ");
        String repoUrl = scanner.nextLine();
        System.out.println("-------------------------------");
        // Repo adını al
        String[] urlParts = repoUrl.split("/");
        String repoFolderName = urlParts[urlParts.length - 1];
        // .git uzantısını ve gereksiz eğik çizgileri kaldır
        repoFolderName = repoFolderName.replace(".git", "").replace("/", "");

        // Clone the repo folder
        try {
            Process process = Runtime.getRuntime().exec("git clone " + repoUrl);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return;
        }
        
        javaFiles = findJavaFiles(repoFolderName);
    }
    
    // Find .java files in the given folder
    private List<String> findJavaFiles(String folderName) {
        List<String> javaFiles = new ArrayList<>();
        findJavaFilesHelper(folderName, javaFiles);
        return javaFiles;
    }

    private void findJavaFilesHelper(String folderName, List<String> javaFiles) {
        File folder = new File(folderName);
        File[] files = folder.listFiles();

        if (files == null)
            return;
        for (File file : files) {
            if (file.isDirectory()) {
                findJavaFilesHelper(file.getAbsolutePath(), javaFiles);
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                if (containsClassKeyword(file)) {
                    javaFiles.add(file.getAbsolutePath());
                }
            }
        }
    }
    

    private static boolean containsClassKeyword(File file) {
        boolean classFound = false;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                
                if (line.matches("^\\s*(public|private|protected|abstract|final)?\\s*class\\s+.*")) {
                    classFound = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classFound;
    }
    

//    public void printJavaFiles() {
//        System.out.println("Bulunan .java dosyaları:");
//        for (String file : javaFiles) {
//            System.out.println(file);
//        }
//    }
    
    public List<String> getJavaFiles() {
        return javaFiles;
    }
    
}
