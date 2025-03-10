package com.yaksha.assignment.functional;

import static com.yaksha.assignment.utils.TestUtils.businessTestFile;
import static com.yaksha.assignment.utils.TestUtils.currentTest;
import static com.yaksha.assignment.utils.TestUtils.testReport;
import static com.yaksha.assignment.utils.TestUtils.yakshaAssert;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.yaksha.assignment.controller.GreetingController;
import com.yaksha.assignment.utils.CustomParser;

public class GreetingControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testShowFormPageAsIndex() throws Exception {
		// Check if the @GetMapping("/") annotation is present on showForm method
		Method method = GreetingController.class.getMethod("showForm");
		GetMapping getMapping = method.getAnnotation(GetMapping.class);

		// Ensure the @GetMapping annotation exists with the correct path value
		boolean isGetMappingCorrect = getMapping != null && getMapping.value()[0].equals("/");

		// Directly call the controller method
		GreetingController controller = new GreetingController();
		String viewName = controller.showForm();

		// Check if the returned view is "index"
		boolean isIndexViewReturned = "index".equals(viewName);

		// Output debugging information
		System.out.println("testShowFormPageAsIndex:");
		System.out.println("Is @GetMapping correct: " + isGetMappingCorrect);
		System.out.println("Is 'index' view returned: " + isIndexViewReturned);

		// Auto-grading with yakshaAssert
		yakshaAssert(currentTest(), isGetMappingCorrect && isIndexViewReturned, businessTestFile);
	}

	@Test
	public void testGreetUser() throws Exception {
		// Check if the @GetMapping("/greet") annotation is present on greetUser method
		Method method = GreetingController.class.getMethod("greetUser", String.class, int.class, Model.class);
		GetMapping getMapping = method.getAnnotation(GetMapping.class);

		// Ensure the @GetMapping annotation exists with the correct path value
		boolean isGetMappingCorrect = getMapping != null && getMapping.value()[0].equals("/greet");

		// Create a mock Model
		Model model = new org.springframework.ui.Model() {
			private java.util.Map<String, Object> attributes = new java.util.HashMap<>();

			@Override
			public Model addAttribute(String attributeName, Object attributeValue) {
				attributes.put(attributeName, attributeValue);
				return this;
			}

			@Override
			public Model addAllAttributes(java.util.Collection<?> attributeValues) {
				return this;
			}

			@Override
			public Model addAllAttributes(java.util.Map<String, ?> attributes) {
				this.attributes.putAll(attributes);
				return this;
			}

			@Override
			public boolean containsAttribute(String attributeName) {
				return attributes.containsKey(attributeName);
			}

			@Override
			public java.util.Map<String, Object> asMap() {
				return attributes;
			}

			@Override
			public Model addAttribute(Object attributeValue) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Model mergeAttributes(Map<String, ?> attributes) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getAttribute(String attributeName) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		// Directly call the controller method
		GreetingController controller = new GreetingController();
		String viewName = controller.greetUser("John", 25, model);

		// Check if the model contains the correct greeting message and the returned
		// view is "greeting"
		boolean isGreetingMessageCorrect = "Hello, John. You are 25 years old!"
				.equals(model.asMap().get("greetingMessage"));
		boolean isGreetingViewReturned = "greeting".equals(viewName);

		// Output debugging information
		System.out.println("testGreetUser:");
		System.out.println("Is @GetMapping correct: " + isGetMappingCorrect);
		System.out.println("Is greeting message correct: " + isGreetingMessageCorrect);
		System.out.println("Is 'greeting' view returned: " + isGreetingViewReturned);

		// Auto-grading with yakshaAssert
		yakshaAssert(currentTest(), isGetMappingCorrect && isGreetingMessageCorrect && isGreetingViewReturned,
				businessTestFile);
	}

	@Test
	public void testJspTagsAndHtmlTagClosureInIndexJsp() throws IOException {
		String filePath = "src/main/webapp/index.jsp";

		// Check for form submission
		boolean hasFormTag = CustomParser.checkJspTagPresence(filePath, "<form");
		boolean hasInputTags = CustomParser.checkJspTagPresence(filePath, "<input");

		// Run auto-grading using yakshaAssert
		yakshaAssert(currentTest(), hasFormTag && hasInputTags, businessTestFile);
	}

	@Test
	public void testJspTagsAndHtmlTagClosureInGreetingJsp() throws IOException {
		String filePath = "src/main/webapp/WEB-INF/views/greeting.jsp";

		// Ensure that greeting message is rendered correctly
		boolean hasGreetingMessage = CustomParser.checkJspTagPresence(filePath, "<h2>");

		// Run auto-grading using yakshaAssert
		yakshaAssert(currentTest(), hasGreetingMessage, businessTestFile);
	}
}
