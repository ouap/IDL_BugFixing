package competition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.runner.JUnitCore;

public class Main {


	public void runTest(String test){
		JUnitCore junit = new JUnitCore();
		//TODO
	}



	private static List<Path> getProjectsPaths() {
		List<Path> results = new ArrayList<>();
		try (Stream<Path> paths = Files.walk(Paths.get("IntroClassJava/dataset"))) {
			List<Path> result = paths.filter(s -> s.toString().endsWith("/src")).collect(Collectors.toList());
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	private static List<File> getJavaFiles(String string) {
		List<String> results = new ArrayList<>();
		try (Stream<Path> paths = Files.walk(Paths.get(string))) {
			List<File> result;

			result = paths.filter(s -> s.toString().endsWith(".java")).map(s-> new File(s.toUri())).collect(Collectors.toList());
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	private static void compileProject(Path path) {
		System.out.println("Compile : " + path.toString());

		List<File> javaFiles = getJavaFiles(path.toString() + "/main/java");
		List<File> testFiles = getJavaFiles(path.toString() + "/test/java");
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		//Get file manager
		StandardJavaFileManager fileManager =
				compiler.getStandardFileManager(null, null, null);
		//Give list of files to compile to fileManager
		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles (javaFiles);
		CompilationTask task = compiler.getTask (null,fileManager, null, null, null, compilationUnits);

		//Compile
		boolean result = task.call();

		if (result) {
			System.out.println ("Compilation was successful");
		} else {
			System.out.println ("Compilation failed");
		}
		try {
			fileManager.close ();
		} catch (IOException e) {
		}

	}


	public static void deleteClass(){
		List<String> results = new ArrayList<>();
		try (Stream<Path> paths = Files.walk(Paths.get("IntroClassJava")).filter(s -> s.toString().endsWith(".class"))) {
			List<File> result = paths.map(p -> new File(p.toUri())).collect(Collectors.toList());

			//Delete class files
			result.forEach(f -> f.delete());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		Main main = new Main();
		List<Path> projectsPaths = getProjectsPaths();

		//Delete all class files
		deleteClass();

		//Compile all projects
		for (Path project : projectsPaths) {
			compileProject(project);
		}





	}



}


