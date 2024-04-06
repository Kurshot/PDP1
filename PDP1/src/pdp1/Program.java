/**
*
* @author Ahmet Kürşat Sonkur - ahmet.sonkur@ogr.sakarya.edu.tr - b211210010
* @since 04/04/2024
* <p>
* Main fonksiyonunun bulunduğu Program class'ı
* </p>
*/
package pdp1;

import java.util.List;

public class Program {

	public static void main(String[] args) {
		GithubRepoCloner gh = new GithubRepoCloner();
		List<String> javaFiles = gh.getJavaFiles();
		ClassOperations co = new ClassOperations(javaFiles);
		
		
	}

}
