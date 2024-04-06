/**
*
* @author Ahmet Kürşat Sonkur - ahmet.sonkur@ogr.sakarya.edu.tr - b211210010
* @since 04/04/2024
* <p>
* İstenilen çıktıların üretilmesi. Fonksiyonlarda genellikle regex kullanılarak çözüme ulaşıldı. 
* </p>
*/
package pdp1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;

public class ClassOperations {
	
	public ClassOperations(List<String> javaFiles){
		
	    int totalLines;
	    int totalCodeLines;
	    int javaDocLines;
	    
	    for(int i = 0; i < javaFiles.size(); i++) 
	    {
	    	printDetails(javaFiles.get(i));
	    	
	    }
	}
    
	public void printDetails(String filePath) {
		// Sınıf : X.java şeklinde print eder.
		System.out.println("Sınıf : " + getFileName(filePath) + ".java");
		System.out.println("Javadoc Satır Sayısı : "+ countJavaDocComments(filePath));
		System.out.println("Yorum Satır Sayısı : " + countComments(filePath));
		System.out.println("Kod Satır Sayısı : " + countCodeLines(filePath));
		System.out.println("Line of Code (LOC) : " + countLines(filePath));
		System.out.println("Fonksiyon Sayısı : "  + countFunctions(filePath));
		System.out.println("Yorum Sapma Yüzdesi : " + "%" +  deviationRate(filePath));
		System.out.println("-------------------------------");
	}
	
	 public static String getFileName(String filePath) {
	        int lastSeparatorIndex = filePath.lastIndexOf(File.separator);
	        int lastDotIndex = filePath.lastIndexOf('.');
	        
	        if (lastSeparatorIndex == -1 || lastDotIndex == -1) {
	            return ""; // Dosya adı alınamadı
	        }
	        
	        return filePath.substring(lastSeparatorIndex + 1, lastDotIndex);
	    }

	private int countLines(String filePath) {
	    int lineCount = 0;
	    try (Scanner readFile = new Scanner(new File(filePath))) {
	        while (readFile.hasNextLine()) {
	            readFile.nextLine();
	            lineCount++;
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return lineCount;
	}
	
	private static int countCodeLines(String filePath) {
        int codeLineCount = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Satırın başındaki ve sonundaki boşlukları kaldır
                line = line.trim();
                
                // Boş satırı kontrol et
                if (line.isEmpty()) {
                    continue; // Boş satırı dahil etme
                }
                
                // Yorum satırını kontrol et
                if (line.startsWith("//") || line.startsWith("/*") || line.startsWith("/**") || line.startsWith("*") || line.startsWith("*/")) {
                    continue; // Yorum satırını dahil etme
                }
                
                // Kod satırı bulundu, sayıyı artır
                codeLineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return codeLineCount;
    }
	
	private  int countJavaDocComments(String filePath) {
		 StringBuilder contentBuilder = new StringBuilder();
		 try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                contentBuilder.append(line);
	                contentBuilder.append("\n"); // Her satırın sonuna yeni satır karakteri ekle
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		 
		 int javaDocCount = 0;

        Pattern pattern = Pattern.compile("/\\*\\*.*?\\*/", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(contentBuilder);

        while (matcher.find()) {
            String commentBlock = matcher.group();
            javaDocCount += countNewLines(commentBlock) - 1; // -->  /** ve */ sayılmayacak (hocanın örneklerinde sayılmıyor) dolayısıyla -1 yap.
        }   
        
        return javaDocCount;
	}
	
	private static int countNewLines(String text) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == '\n') {
                count++;
            }
        }
        return count;
    }
	
	private int countComments(String filePath) {
		return countMultipleComments(filePath) + countSingleLineComments(filePath);
	}
	
	private int countMultipleComments(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();
		 try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                contentBuilder.append(line);
	                contentBuilder.append("\n"); // Her satırın sonuna yeni satır karakteri ekle
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		 
		 int multiCommentCount = 0;

		Pattern pattern = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(contentBuilder);

        while (matcher.find()) {
            String commentBlock = matcher.group();
            if (!commentBlock.startsWith("/**")) { // Javadoc bloğu değilse
            	multiCommentCount += countNewLines(commentBlock) - 1;
            }
        }

        return multiCommentCount;
	}
	
	 private static int countSingleLineComments(String filePath) {
		 StringBuilder contentBuilder = new StringBuilder();
		 try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                contentBuilder.append(line);
	                contentBuilder.append("\n"); // Her satırın sonuna yeni satır karakteri ekle
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		 Pattern pattern = Pattern.compile("//.*?(?=\\n|$)", Pattern.DOTALL);
	        Matcher matcher = pattern.matcher(contentBuilder);
	        int count = 0;
	        while (matcher.find()) {
	            count++;
	        }
	        return count;
	    }
	 
	 private static int countFunctions(String filePath) {
		    StringBuilder contentBuilder = new StringBuilder();
		    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
		        String line;
		        while ((line = reader.readLine()) != null) {
		            contentBuilder.append(line);
		            contentBuilder.append("\n"); // Her satırın sonuna yeni satır karakteri ekle
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }

		    Pattern pattern = Pattern.compile("((public|private|protected|static|final|\\s)*\\s+)?\\w+\\s+\\w+\\s*\\(.*?\\)\\s*\\{", Pattern.DOTALL);
		    Matcher matcher = pattern.matcher(contentBuilder);

		    int count = 0;
		    while (matcher.find()) {
		        count++;
		    }

		    return count;
		}
	 
	 private double deviationRate(String filePath) {
		 double YG = ( (countJavaDocComments(filePath) + countComments(filePath)) * 0.8 )    / countFunctions(filePath)  ;
		 double YH =  ((double)countCodeLines(filePath)/(double)countFunctions(filePath) * 0.3) ;
		 double deviationRate =  round(( (100 * YG) / YH ) - 100,2) ;
		 return deviationRate;
	 }
	 
	 public static double round(double value, int places) {
		    if (places < 0) throw new IllegalArgumentException();

		    BigDecimal bd = BigDecimal.valueOf(value);
		    bd = bd.setScale(places, RoundingMode.HALF_UP);
		    return bd.doubleValue();
		}
	 
	
	 

}
