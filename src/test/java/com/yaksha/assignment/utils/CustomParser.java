package com.yaksha.assignment.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;

public class CustomParser {

	/**
	 * Checks if the class contains the required class-level annotation.
	 * 
	 * @param filePath        - Full path to the class file.
	 * @param classAnnotation - The annotation to check for in the class
	 *                        (e.g., @SpringBootApplication).
	 * @return true if the class has the annotation, false otherwise.
	 * @throws IOException
	 */
	public static boolean checkClassAnnotation(String filePath, String classAnnotation) throws IOException {
		System.out.println("Checking class-level annotation in file: " + filePath);

		// Load class content
		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			System.out.println("Error: Failed to load class content from file: " + filePath);
			return false;
		}

		// Parse the class content using JavaParser
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content from file: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if the class contains the required annotation
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));

		if (classDeclaration.isEmpty()) {
			System.out.println("Error: Class not found in the provided file.");
			return false;
		}

		boolean hasClassAnnotation = classDeclaration.get().getAnnotations().stream()
				.anyMatch(annotation -> annotation.getNameAsString().equals(classAnnotation));

		if (!hasClassAnnotation) {
			System.out.println("Error: The class is missing the @" + classAnnotation + " annotation.");
			return false;
		}

		System.out.println("Class is annotated with @" + classAnnotation + " annotation.");
		return true;
	}

	/**
	 * Checks if any method contains the required method-level annotation.
	 * 
	 * @param filePath         - Full path to the class file.
	 * @param methodAnnotation - The annotation to check for in methods
	 *                         (e.g., @Autowired).
	 * @return true if any method contains the annotation, false otherwise.
	 * @throws IOException
	 */
	public static boolean checkMethodAnnotation(String filePath, String methodAnnotation) throws IOException {
		System.out.println("Checking method-level annotation in file: " + filePath);

		// Load class content
		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			System.out.println("Error: Failed to load class content from file: " + filePath);
			return false;
		}

		// Parse the class content using JavaParser
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content from file: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if any method contains the required annotation
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));

		if (classDeclaration.isEmpty()) {
			System.out.println("Error: Class not found in the provided file.");
			return false;
		}

		boolean hasMethodAnnotation = classDeclaration.get().getMethods().stream()
				.anyMatch(method -> method.getAnnotationByName(methodAnnotation).isPresent());

		if (!hasMethodAnnotation) {
			System.out.println("Error: No method is annotated with @" + methodAnnotation + " annotation.");
			return false;
		}

		System.out.println("Method is annotated with @" + methodAnnotation + " annotation.");
		return true;
	}

	/**
	 * Checks if any constructor contains the required constructor-level annotation.
	 * 
	 * @param filePath              - Full path to the class file.
	 * @param constructorAnnotation - The annotation to check for in constructors
	 *                              (e.g., @Autowired).
	 * @return true if any constructor contains the annotation, false otherwise.
	 * @throws IOException
	 */
	public static boolean checkConstructorAnnotation(String filePath, String constructorAnnotation) throws IOException {
		System.out.println("Checking constructor-level annotation in file: " + filePath);

		// Load class content
		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			System.out.println("Error: Failed to load class content from file: " + filePath);
			return false;
		}

		// Parse the class content using JavaParser
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content from file: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if any constructor contains the required annotation
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));

		if (classDeclaration.isEmpty()) {
			System.out.println("Error: Class not found in the provided file.");
			return false;
		}

		boolean hasConstructorAnnotation = classDeclaration.get().getConstructors().stream()
				.anyMatch(constructor -> constructor.getAnnotationByName(constructorAnnotation).isPresent());

		if (!hasConstructorAnnotation) {
			System.out.println("Error: No constructor is annotated with @" + constructorAnnotation + " annotation.");
			return false;
		}

		System.out.println("Constructor is annotated with @" + constructorAnnotation + " annotation.");
		return true;
	}

	/**
	 * Loads the content of the class file from the file path.
	 * 
	 * @param filePath Full path to the class file.
	 * @return The class content as a String.
	 * @throws IOException
	 */
	private static String loadClassContent(String filePath) throws IOException {
		// Create a File object from the provided file path
		File participantFile = new File(filePath);
		if (!participantFile.exists()) {
			System.out.println("Error: Class file not found: " + filePath);
			return null;
		}

		// Read the content of the file
		try (FileInputStream fileInputStream = new FileInputStream(participantFile)) {
			byte[] bytes = fileInputStream.readAllBytes();
			return new String(bytes, StandardCharsets.UTF_8);
		}
	}

	/**
	 * Checks if a field contains a specific annotation (e.g., @Autowired).
	 * 
	 * @param filePath   - Full path to the class file.
	 * @param fieldType  - The type of the field to check (e.g., "Order").
	 * @param annotation - The annotation to check for (e.g., "Autowired").
	 * @return true if the field has the annotation, false otherwise.
	 * @throws IOException
	 */
	public static boolean checkFieldAnnotation(String filePath, String fieldType, String annotation)
			throws IOException {
		System.out.println("Checking field-level annotation on field of type '" + fieldType + "' in file: " + filePath);

		// Load class content
		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			System.out.println("Error: Failed to load class content from file: " + filePath);
			return false;
		}

		// Parse the class content using JavaParser
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content from file: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if the class contains the required field annotation
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));

		if (classDeclaration.isEmpty()) {
			System.out.println("Error: Class not found in the provided file.");
			return false;
		}

		// Iterate over all fields in the class
		boolean hasFieldAnnotation = classDeclaration.get().getFields().stream()
				.flatMap(field -> field.getVariables().stream()) // Loop through variables of the field
				.filter(var -> var.getTypeAsString().equals(fieldType)) // Match field type (e.g., "Order")
				.anyMatch(field -> field.getParentNode().isPresent()
						&& field.getParentNode().get() instanceof FieldDeclaration
						&& ((FieldDeclaration) field.getParentNode().get()).getAnnotations().stream()
								.anyMatch(annotationNode -> annotationNode.getNameAsString().equals(annotation))); // Match
																													// annotation

		if (!hasFieldAnnotation) {
			System.out.println(
					"Error: The field of type '" + fieldType + "' is missing the @" + annotation + " annotation.");
			return false;
		}

		System.out.println("Field of type '" + fieldType + "' is annotated with @" + annotation + " annotation.");
		return true;
	}

	/**
	 * Checks if a constructor contains a specific annotation (e.g., @Value).
	 * 
	 * @param filePath              - Full path to the class file.
	 * @param constructorAnnotation - The annotation to check for in constructor
	 *                              parameters (e.g., "Value").
	 * @param parameterNames        - Names of the parameters to check.
	 * @return true if the constructor contains the annotation on the specified
	 *         parameters, false otherwise.
	 * @throws IOException
	 */
	public static boolean checkConstructorParameterAnnotation(String filePath, String constructorAnnotation,
			String... parameterNames) throws IOException {
		System.out.println(
				"Checking constructor parameters for annotation @" + constructorAnnotation + " in file: " + filePath);

		// Load class content
		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			System.out.println("Error: Failed to load class content from file: " + filePath);
			return false;
		}

		// Parse the class content using JavaParser
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content from file: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if the class contains the required constructor annotation
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));

		if (classDeclaration.isEmpty()) {
			System.out.println("Error: Class not found in the provided file.");
			return false;
		}

		// Iterate over constructors to find the matching annotations on parameters
		boolean hasConstructorAnnotation = classDeclaration.get().getConstructors().stream().anyMatch(constructor -> {
			// Check if constructor parameters have the specified annotation
			return constructor.getParameters().stream()
					.anyMatch(param -> parameterNames.length == 2 && param.getNameAsString().equals(parameterNames[0])
							&& param.getAnnotations().stream().anyMatch(
									annotation -> annotation.getNameAsString().equals(constructorAnnotation)));
		});

		if (!hasConstructorAnnotation) {
			System.out.println(
					"Error: The constructor is missing the @" + constructorAnnotation + " annotation on parameters.");
			return false;
		}

		System.out.println("Constructor parameters are annotated with @" + constructorAnnotation + " annotation.");
		return true;
	}

	public static boolean checkClassAnnotationWithValue(String filePath, String classAnnotation, String annotationValue)
			throws IOException {
		System.out.println("Checking class-level annotation value in file: " + filePath);

		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			return false;
		}

		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();
		if (optionalCompilationUnit.isEmpty()) {
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));
		if (classDeclaration.isEmpty()) {
			return false;
		}

		return classDeclaration.get().getAnnotations().stream()
				.anyMatch(annotation -> annotation.getNameAsString().equals(classAnnotation)
						&& annotation.toString().contains(annotationValue));
	}

	/**
	 * Checks if any method contains the required method-level annotation with a
	 * specific value.
	 *
	 * @param filePath         - Full path to the class file.
	 * @param methodAnnotation - The annotation to check for in methods
	 *                         (e.g., @Qualifier).
	 * @param annotationValue  - The expected value of the annotation (e.g.,
	 *                         "payPalPaymentGateway").
	 * @return true if any method contains the annotation with the specific value,
	 *         false otherwise.
	 * @throws IOException
	 */
	public static boolean checkMethodAnnotationWithValue(String filePath, String methodAnnotation,
			String annotationValue) throws IOException {
		System.out.println("Checking method-level annotation with value in file: " + filePath);

		// Load class content
		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			System.out.println("Error: Failed to load class content from file: " + filePath);
			return false;
		}

		// Parse the class content using JavaParser
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content from file: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if any method contains the required annotation and value
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));

		if (classDeclaration.isEmpty()) {
			System.out.println("Error: Class not found in the provided file.");
			return false;
		}

		boolean hasMethodAnnotationWithValue = classDeclaration.get().getMethods().stream()
				.anyMatch(method -> method.getAnnotationByName(methodAnnotation).isPresent()
						&& method.getAnnotationByName(methodAnnotation).get().getChildNodes().stream()
								.anyMatch(node -> node.toString().contains(annotationValue)));

		if (!hasMethodAnnotationWithValue) {
			System.out.println("Error: No method is annotated with @" + methodAnnotation + " with value '"
					+ annotationValue + "'.");
			return false;
		}

		System.out.println("Method is annotated with @" + methodAnnotation + " with value '" + annotationValue + "'.");
		return true;
	}

	/**
	 * Extracts the class name from the file path (assumes the class name is the
	 * same as the file name).
	 * 
	 * @param filePath The path to the Java file.
	 * @return The class name (without package).
	 */
	private static String getClassName(String filePath) {
		// Extract class name from file path (assumes Java file name matches the class
		// name)
		String fileName = new File(filePath).getName();
		return fileName.substring(0, fileName.lastIndexOf('.'));
	}

	/**
	 * Checks if a specific method contains a parameter annotated with @Qualifier
	 * and with a specific value.
	 *
	 * @param filePath        - Full path to the class file.
	 * @param methodName      - The name of the method to check for the annotation.
	 * @param annotation      - The annotation to check for (e.g., "Qualifier").
	 * @param annotationValue - The expected value of the annotation (e.g.,
	 *                        "payPalPaymentGateway").
	 * @return true if the specified method contains a parameter with the @Qualifier
	 *         annotation and correct value, false otherwise.
	 * @throws IOException
	 */
	public static boolean checkMethodParameterAnnotationWithValue(String filePath, String methodName, String annotation,
			String annotationValue) throws IOException {
		System.out.println("Checking if method '" + methodName + "' has a parameter annotated with @" + annotation
				+ " with value: " + annotationValue);

		// Load class content
		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			System.out.println("Error: Failed to load class content from file: " + filePath);
			return false;
		}

		// Parse the class content using JavaParser
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content from file: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if the class contains the specified method
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));

		if (classDeclaration.isEmpty()) {
			System.out.println("Error: Class not found in the provided file.");
			return false;
		}

		// Check if the specified method contains a parameter annotated with @Qualifier
		// and the correct value
		boolean hasQualifierAnnotation = classDeclaration.get().getMethodsByName(methodName).stream()
				.flatMap(method -> method.getParameters().stream()) // Loop through method parameters
				.anyMatch(param -> param.getAnnotations().stream() // Check if parameter has the @Qualifier annotation
						.anyMatch(annotationNode -> annotationNode.getNameAsString().equals(annotation)
								&& annotationNode.getChildNodes().stream()
										.anyMatch(node -> node.toString().contains(annotationValue))));

		if (!hasQualifierAnnotation) {
			System.out.println("Error: The method '" + methodName + "' does not have a parameter annotated with @"
					+ annotation + " with value '" + annotationValue + "'.");
			return false;
		}

		System.out.println("Method '" + methodName + "' has a parameter annotated with @" + annotation + " with value '"
				+ annotationValue + "'.");
		return true;
	}

	/**
	 * Reads a JSP file as raw text and checks if a specified tag is present.
	 *
	 * @param filePath The relative path to the JSP file.
	 * @param tag      The JSP tag to search for.
	 * @return true if the tag is present, false otherwise.
	 * @throws IOException if file reading fails.
	 */
	public static boolean checkJspTagPresence(String filePath, String tag) throws IOException {
		File file = new File(filePath);

		System.out.println("Checking file: " + filePath);

		// Ensure file exists
		if (!file.exists()) {
			System.err.println("Error: JSP file does not exist at path - " + filePath);
			return false;
		}

		// Read file as raw text
		String jspContent = new String(Files.readAllBytes(Paths.get(filePath)));

		// Check if the tag is present
		boolean containsTag = jspContent.contains(tag);

		if (containsTag) {
			System.out.println("Success: Found tag [" + tag + "] in file " + filePath);
		} else {
			System.err.println("Error: Tag [" + tag + "] not found in file " + filePath);
		}

		return containsTag;
	}

	/**
	 * Checks if a given HTML tag is present and properly closed in a JSP/HTML file.
	 *
	 * @param filePath The relative file path.
	 * @param tag      The HTML tag to check (e.g., "div", "table", "p").
	 * @return True if the tag is present and properly closed, false otherwise.
	 * @throws IOException If file reading fails.
	 */
	public static boolean isTagProperlyClosedInFile(String filePath, String tag) throws IOException {
		File file = new File(filePath);

		// Ensure the file exists before parsing
		if (!file.exists()) {
			System.err.println("Error: File does not exist at path - " + filePath);
			return false;
		}

		// Parse the HTML file using JSoup
		Document document = Jsoup.parse(file, "UTF-8");

		// Select all occurrences of the specified tag
		Elements elements = document.select(tag);

		// If at least one properly closed tag is found, return true
		boolean isClosedProperly = !elements.isEmpty();

		if (isClosedProperly) {
			System.out.println("Success: Found properly closed <" + tag + "> tag in " + filePath);
		} else {
			System.err.println("Error: No properly closed <" + tag + "> tag found in " + filePath);
		}

		return isClosedProperly;
	}

	/**
	 * Checks if a method in JobController is annotated with @GetMapping and has the
	 * correct value.
	 *
	 * @param methodName    The name of the method to check.
	 * @param paramTypes    The parameter types of the method.
	 * @param expectedValue The expected value of @GetMapping.
	 * @return True if the annotation is present and has the correct value, false
	 *         otherwise.
	 */
	public static boolean checkMethodAnnotation(String methodName, Class<?>[] paramTypes, String expectedValue) {
		try {
			Method method = com.yaksha.assignment.controller.GreetingController.class.getMethod(methodName, paramTypes);

			// Check if @GetMapping annotation is present
			GetMapping getMapping = method.getAnnotation(GetMapping.class);
			if (getMapping == null) {
				System.err.println("❌ ERROR: @GetMapping annotation missing on method: " + methodName);
				return false;
			}

			// Check if the annotation has the expected value
			if (getMapping.value().length == 0 || !expectedValue.equals(getMapping.value()[0])) {
				System.err.println("❌ ERROR: @GetMapping value is incorrect for method: " + methodName + " | Expected: "
						+ expectedValue + " | Found: "
						+ (getMapping.value().length > 0 ? getMapping.value()[0] : "NONE"));
				return false;
			}

			System.out.println("✅ SUCCESS: @GetMapping is correctly applied on method: " + methodName + " with value: "
					+ expectedValue);
			return true;

		} catch (NoSuchMethodException e) {
			System.err.println("❌ ERROR: Method not found - " + methodName);
			return false;
		}
	}
}
