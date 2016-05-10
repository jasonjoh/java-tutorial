## Before you begin

You need to install the [Java SE Development Kit (JDK)](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html#javasejdk). This guide was written with JDK 8 Update 92.

If you want to follow along exactly, download [Spring Tool Suite](http://spring.io/tools/sts). This guide was written with version 3.7.3.RELEASE. You can use any IDE you want, but you might have to do some steps differently.

> **NOTE**: If must install the version of Spring Tool Suite (either 64-bit or 32-bit) that corresponds to the version of the JDK you installed. For example, if you install the 64-bit JDK, install the 64-bit version of Spring Tool Suite.

### Spring Tool Suite setup

If this is your first time using Spring Tool Suite, you should complete the following steps to configure it to work with your downloaded JDK, and to make adding dependencies with Maven easier.

1. Launch Spring Tool Suite. On the **Window** menu, choose **Preferences**.

1. Expand **Java** and select **Installed JREs**. Click **Add**.

1. Choose **Standard VM** and click **Next**.

1. Click the **Directory** button next to **JRE home**. Browse to the directory where you installed the JDK. Click **Finish**.

  ![The Add JRE dialog in Spring Tool Suites](./readme-images/installed-jre.PNG)
1. In the list of **Installed JREs**, make sure that the entry you just added is checked, making it the default. Click **OK**.

1. On the **Window** menu, choose **Show View**, then **Other**. Choose **Maven Repositories** from the list of views and click **OK**.

1. In the **Maven Repositories** window, expand **Global Repositories**. Right-click the **central** item there and choose **Rebuild Index**. This takes a few minutes, so let it complete before continuing.

## Create the app ##

In Spring Tool Suite, on the **File** menu, choose **New**, then **Other**. In the list of wizards, expand **Maven**, then choose **Maven Project**. Click **Next**.

Check the **Create a simple project (skip archetype selection)** checkbox and click **Next**.

Enter 'com.outlook.dev' for **Group Id**, and `java-tutorial` for **Artifact Id**. Change **Packaging** to `war`. Click **Finish**.

![The New Maven project dialog in Spring Tool Suite](./readme-images/new-maven-project.PNG)

In **Project Explorer**, right-click the project and choose **Properties**. Select **Java Build Path**, then select the **Libraries** tab. If the version of the **JRE System Library** does not match the version of the JDK you installed, do the following:

1. Select the **JRE System Library** and click **Remove**. 

1. Click **Add Library**. Choose **JRE System Library** and click **Next**.

1. Select **Workspace default JRE** and click **Finish**.

  ![The Java Build Path dialog in Spring Tool Suite](./readme-images/java-build-path.PNG)
  
1. Click **OK**.

Right-click the project and choose **Properties**. Select **Project Facets**. Change the version of **Dynamic Web Module** to `3.0`. Change the version of **Java** to `1.8`.

![The Project Facets dialog in Spring Tool Suite](./readme-images/project-facets.PNG)

Click **OK**. In order to force Spring Tool Suite to recognize the facet changes, we need to close and reopen the project. Right-click the project and choose **Close Project**. Then double-click (or right-click and choose **Open Project**) to reopen the project.

In **Project Explorer** right-click the **Deployment Descriptor: java-tutorial** item and choose **Generate Deployment Descriptor Stub**.

Now let's configure the project to build with Maven and run on a local test server. To do this, let's add some plugins in `pom.xml`. Open `pom.xml`, then click the **pom.xml** tab at the bottom of the window to view the raw XML. Add the following code immediately after the `<packaging>war</packaging>` line:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.eclipse.jetty</groupId>
      <artifactId>jetty-maven-plugin</artifactId>
      <version>9.3.8.v20160314</version>
    </plugin>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.5.1</version>
      <configuration>
        <source>1.8</source>
        <target>1.8</target>
      </configuration>
    </plugin>
  </plugins>
</build>
```

This code adds the [Jetty server maven plugin](http://www.eclipse.org/jetty/documentation/current/jetty-maven-plugin.html), which runs your project on an embedded Jetty web server at http://localhost:8080. It also adds the Maven compiler plugin.

Save the file. Right-click the project in **Project Explorer** and choose **Maven**, then **Update Project**. Click **OK**.

Now let's make sure it works. Right-click the project and choose **Run as**, then **Maven build**. In the **Goals** field, enter `jetty:run`, then click **Run**. In the **Console** view, you should see output from the Maven build process. Assuming nothing goes wrong, you should eventually see `[INFO] Started Jetty Server`. Open a browser and browse to http://localhost:8080. Because there is no content there, you should see something like this:

![The default landing page for the Jetty test server](./readme-images/jetty-default.PNG)

### Add Spring and Apache Tiles ###

Now let's add the [Spring Framework](http://projects.spring.io/spring-framework/) and [Apache Tiles](http://tiles.apache.org/). The Spring Framework will simplify our web app development and give us and MVC-based environment, and Apache Tiles will enable our pages to reuse a standard layout.

In `pom.xml`, add the following lines after the `</build>` line:

```xml
<properties>
  <org.springframework.version>4.2.5.RELEASE</org.springframework.version>
  <apache.tiles.version>3.0.5</apache.tiles.version>
</properties>

<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-framework-bom</artifactId>
      <version>${org.springframework.version}</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependencies>
  <dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.0-b01</version>
    <scope>provided</scope>
  </dependency>
  
  <dependency>
    <groupId>javax.servlet.jsp</groupId>
    <artifactId>javax.servlet.jsp-api</artifactId>
    <version>2.3.2-b02</version>
    <scope>provided</scope>
  </dependency>
  
  <dependency>
    <groupId>jstl</groupId>
    <artifactId>jstl</artifactId>
    <version>1.2</version>
  </dependency>
  
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-expression</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-beans</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aop</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context-support</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-tx</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-orm</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-oxm</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
  </dependency>
  
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc-portlet</artifactId>
  </dependency>
  
  <dependency>
    <groupId>org.apache.tiles</groupId>
    <artifactId>tiles-core</artifactId>
    <version>${apache.tiles.version}</version>
  </dependency>
  
  <dependency>
    <groupId>org.apache.tiles</groupId>
    <artifactId>tiles-jsp</artifactId>
    <version>${apache.tiles.version}</version>
  </dependency>
  
  <dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.21</version>
  </dependency>
</dependencies>
```

Save the file. Right-click the project in **Project Explorer** and choose **Maven**, then **Update Project**. Click **OK**.

Now let's create the basic project structure and make sure that it is working. In **Project Explorer**, expand **Deployed Resources**, then **webapp**, and **WEB-INF**. Open the `web.xml` file, and add the following code after the `</welcome-file-list>` line:

```xml
<servlet>
  <servlet-name>dispatcher</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
  <servlet-name>dispatcher</servlet-name>
  <url-pattern>*.html</url-pattern>
  <url-pattern>*.htm</url-pattern>
  <url-pattern>*.json</url-pattern>
  <url-pattern>*.xml</url-pattern>
</servlet-mapping>
```

Right-click the **WEB-INF** folder and choose **New**, then **Other**. In the list of wizards, expand **Spring**, then choose **Spring Bean Configuration File** and click **Next**. Name the file `dispatcher-servlet.xml` and click **Finish**. The file should be created and opened for you.

Click the **Namespaces** tab in the bottom toolbar. Select the `context` namespace, then click the **Source** tab.

![](./readme-images/add-context-namespace.PNG)

Add the following code inside the `<beans>` element:

```xml
<context:component-scan base-package="com.outlook.dev.controller"></context:component-scan>

<bean id="tilesConfigurer"
  class="org.springframework.web.servlet.view.tiles3.TilesConfigurer">
  <property name="definitions">
    <list>
      <value>/WEB-INF/defs/pages.xml</value>
    </list>
  </property>
</bean>

<bean id="viewResolver"
  class="org.springframework.web.servlet.view.UrlBasedViewResolver">
  <property name="viewClass"
    value="org.springframework.web.servlet.view.tiles3.TilesView" />
</bean>
```

This tells the Spring Framework where to find the controllers for the app, enables Apache Tiles, and sets where page definitions are stored (in `pages.xml`). Let's create the `pages.xml` file. Right-click the **WEB-INF** folder and choose **New**, then **Folder**. Name the folder `defs` and click **Finish**. Right-click the **defs** folder and choose **New**, then **File**. Name the file `pages.xml` and click **Finish**. Enter the following code in the file and save it.

```xml
<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE tiles-definitions PUBLIC
       "-//Apache Software Foundation//DTD Tiles Configuration 3.0//EN"
       "http://tiles.apache.org/dtds/tiles-config_3_0.dtd">
<tiles-definitions>
  <definition name="common" template="/WEB-INF/layout/base.jsp" />
  
  <definition name="index" extends="common">
    <put-attribute name="title" value="Java Mail API Tutorial" />
    <put-attribute name="body" value="/WEB-INF/jsp/index.jsp" />
    <put-attribute name="current" value="index" />
  </definition>
</tiles-definitions>
```

Now let's create the base layout. Right-click the **WEB-INF** folder and choose **New**, then **Folder**. Name the folder `layout` and click **Finish**. Right-click the **layout** folder and choose **New**, then **JSP File**. Name the file `base.jsp` and click **Finish**. Replace the entire contents of `base.jsp` with the following code:

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles-extras" prefix="tilesx" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><tiles:getAsString name="title"></tiles:getAsString></title>
	
	<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
	<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css">
	
	<script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-2.2.3.min.js"></script>
	<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>
<body>
	<tilesx:useAttribute name="current" />
	<div class="container">

		<!-- Static navbar -->
		<nav class="navbar navbar-default">
			<div class="container-fluid">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed"
						data-toggle="collapse" data-target="#navbar" aria-expanded="false"
						aria-controls="navbar">
						<span class="sr-only">Toggle navigation</span> <span
							class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="<spring:url value="/" />">Java Outlook Tutorial</a>
				</div>
				<div id="navbar" class="navbar-collapse collapse">
					<ul class="nav navbar-nav">
						<li class="${current == 'index' ? 'active' : '' }">
              <a href="<spring:url value="/" />">Home</a>
            </li>
						<c:if test="${userConnected eq true}">
							<li class="${current == 'mail' ? 'active' : '' }">
                <a href="<spring:url value="/mail.html" />">Mail</a>
              </li>
						</c:if>
					</ul>
					<c:if test="${userConnected eq true}">
						<p class="navbar-text navbar-right">Signed in as ${userName}</p>
						<ul class="nav navbar-nav navbar-right">
							<li>
                <a href="<spring:url value="/logout.html" />">Logout</a>
              </li>
						</ul>
					</c:if>
				</div>
			</div>
		</nav>

		<tiles:insertAttribute name="body" />
	</div>
</body>
</html>
```

Let's create a test page for verification purposes. Right-click the **WEB-INF** folder and choose **New**, then **Folder**. Name the folder `jsp` and click **Finish**. Right-click the **jsp** folder and choose **New**, then **JSP File**. Name the file `index.jsp` and click **Finish**. Replace the entire contents of `index.jsp` with the following code:

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<h1>Hello World</h1>
```

Now let's create an empty HTML file in the root. This is required so that the embedded Jetty server will properly invoke our dispatcher servlet. In **Project Explorer**, expand **src**, then **main**. Right-click the **webapp** folder and choose **New**, then **HTML File**. Name the file `index.html` and click **Finish**. Remove all markup from the file and save it.

Now let's create a controller. Right-click the project and choose **New**, then **Class**. Enter `com.outlook.dev.controller` for the **Package** and `IndexController` for the **Name** and click **Finish**. Replace the entire contents of `IndexController.java` with the following code:

```java
package com.outlook.dev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping("/index")
	public String index() {
		// Name of a definition in WEB-INF/defs/pages.xml
		return "index";
	}
}
```

Save all of the files and restart the app. Now if you browse to http://localhost:8080, you should see the following:

![](./readme-images/hello-world.PNG)

Now that we have the environment ready, we're ready to start coding!

## Designing the app ##

Our app will be very simple. When a user visits the site, they will see a button to log in and view their email. Clicking that button will take them to the Azure login page where they can login with their Office 365 or Outlook.com account and grant access to our app. Finally, they will be redirected back to our app, which will display a list of the most recent email in the user's inbox.

Let's begin by creating the homepage. Open the `index.jsp` you created earlier, and replace the entire contents with the following:

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="jumbotron">
	<h1>Java Web App Tutorial</h1>
	<p>This sample uses the Mail API to read messages in your inbox.</p>
	<p><a class="btn btn-lg btn-primary" href="<spring:url value="#" />">Click here to login</a></p>
</div>
```

Now we have a login button. It doesn't do anything yet, but that's about to change.

## Implementing OAuth2 ##

Our goal in this section is to make the link on our home page initiate the [OAuth2 Authorization Code Grant flow with Azure AD](https://msdn.microsoft.com/en-us/library/azure/dn645542.aspx). To make things easier, we'll use the [Microsoft.Experimental.IdentityModel.Clients.ActiveDirectory Prerelease NuGet package](https://www.nuget.org/packages/Microsoft.Experimental.IdentityModel.Clients.ActiveDirectory/4.0.208020147-alpha) to handle our OAuth requests.

Before we proceed, we need to register our app to obtain a client ID and secret. Head over to https://apps.dev.microsoft.com to quickly get a client ID and secret. Using the sign in buttons, sign in with either your Microsoft account (Outlook.com), or your work or school account (Office 365).

Once you're signed in, click the **Add an app** button. Enter `java-tutorial` for the name and click **Create application**. After the app is created, locate the **Application Secrets** section, and click the **Generate New Password** button. Copy the password now and save it to a safe place. Once you've copied the password, click **Ok**.

![The new password dialog.](./readme-images/new-password.PNG)

Locate the **Platforms** section, and click **Add Platform**. Choose **Web**, then enter `http://localhost:8080/authorize` under **Redirect URIs**. Click **Save** to complete the registration. Copy the **Application Id** and save it along with the password you copied earlier. We'll need those values soon.

Here's what the details of your app registration should look like when you are done.

![The completed registration properties.](./readme-images/app-registration.PNG)

In **Project Explorer**, expand **Java Resources**. Right-click **src/main/resources** and choose **New**, then **Other**. Expand **General** and choose **File**. Name the file `auth.properties` and click **Finish**. Add the following lines to the file, replacing `YOUR_APP_ID_HERE` with your application ID, and `YOUR_APP_PASSWORD_HERE` with your application password.

```INI
appId=YOUR_APP_ID_HERE
appPassword=YOUR_APP_PASSWORD_HERE
redirectUrl=http://localhost:8080/authorize
```

Now let's create a class to handle the authentication work. Right-click the project and choose **New**, then **Class**. Change the value of **Package** to `com.outlook.dev.auth`, and name the class `AuthHelper`, then click **Finish**. Replace the entire contents of the `AuthHelper.java` file with the following code:

```java
package com.outlook.dev.auth;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import org.springframework.web.util.UriComponentsBuilder;

public class AuthHelper {
	private static final String authority = "https://login.microsoftonline.com";
	private static final String authorizeUrl = authority + "/common/oauth2/v2.0/authorize";
	private static final String tokenUrl = authority + "/%s/oauth2/v2.0/token";
	
	private static String[] scopes = { 
		"openid", 
		"offline_access",
		"profile", 
		"https://outlook.office.com/mail.read"
	};
	
	private static String appId = null;
	private static String appPassword = null;
	private static String redirectUrl = null;
	
	private static String getAppId() {
		if (appId == null) {
			try {
				loadConfig();
			} catch (Exception e) {
				return null;
			}
		}
		return appId;
	}
	private static String getAppPassword() {
		if (appPassword == null) {
			try {
				loadConfig();
			} catch (Exception e) {
				return null;
			}
		}
		return appPassword;
	}
	
	private static String getRedirectUrl() {
		if (redirectUrl == null) {
			try {
				loadConfig();
			} catch (Exception e) {
				return null;
			}
		}
		return redirectUrl;
	}
	
	private static String getScopes() {
		StringBuilder sb = new StringBuilder();
		for (String scope: scopes) {
			sb.append(scope + " ");
		}
		return sb.toString().trim();
	}
	
	private static void loadConfig() throws IOException {
		String authConfigFile = "auth.properties";
		InputStream authConfigStream = AuthHelper.class.getClassLoader().getResourceAsStream(authConfigFile);
		
		if (authConfigStream != null) {
			Properties authProps = new Properties();
			try {
				authProps.load(authConfigStream);
				appId = authProps.getProperty("appId");
				appPassword = authProps.getProperty("appPassword");
			} finally {
				authConfigStream.close();
			}
		}
		else {
			throw new FileNotFoundException("Property file '" + authConfigFile + "' not found in the classpath.");
		}
	}
	
	public static String getLoginUrl(UUID state, UUID nonce) {
		
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(authorizeUrl);
		urlBuilder.queryParam("client_id", getAppId());
		urlBuilder.queryParam("redirect_uri", getRedirectUrl());
		urlBuilder.queryParam("response_type", "code id_token");
		urlBuilder.queryParam("scope", getScopes());
		urlBuilder.queryParam("state", state);
		urlBuilder.queryParam("nonce", nonce);
		urlBuilder.queryParam("response_mode", "form_post");
		
		return urlBuilder.toUriString();
	}
}
```

Now let's update the `index` function in `IndexController.java` to use the `AuthHelper` class to generate a login URL. Add the following `import` statements in `IndexController.java`:

```java
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.outlook.dev.auth.AuthHelper;
```

Replace the existing `index` function with this updated version:

```java
@RequestMapping("/index")
public String index(Model model, HttpServletRequest request) {
  UUID state = UUID.randomUUID();
  UUID nonce = UUID.randomUUID();
  
  // Save the state and nonce in the session so we can
  // verify after the auth process redirects back
  HttpSession session = request.getSession();
  session.setAttribute("expected_state", state);
  session.setAttribute("expected_nonce", nonce);
  
  String loginUrl = AuthHelper.getLoginUrl(state, nonce);
  model.addAttribute("loginUrl", loginUrl);
  // Name of a definition in WEB-INF/defs/pages.xml
  return "index";
}
```

Now let's create a new controller to handle the redirect back to our app once the user has authenticated. In **Project Explorer**, expand **Java Resources**, then **src/main/java**. Right-click **com.outlook.dev.controller** and choose **New**, then **Class**. Name the class `AuthorizeController` and click **Finish**. Replace the entire contents of the `AuthorizeController.java` file with the following code:

```java
package com.outlook.dev.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

	@RequestMapping(value="/authorize", method=RequestMethod.POST)
	public String authorize(
			@RequestParam("code") String code, 
			@RequestParam("state") UUID state,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		// Get the expected state value from the session
		HttpSession session = request.getSession();
		UUID expectedState = (UUID) request.getSession().getAttribute("expected_state");
		
		// Make sure that the state query parameter returned matches
		// the expected state
		if (state.equals(expectedState)) {
			session.setAttribute("authCode", code);
		}
		else {
			session.setAttribute("error", "Unexpected state returned from authority.");
		}
		return "mail";
	}
}
```

Finally let's create the `mail` view. Right-click the **jsp** (**Deployed Resources**, **webapp**, **WEB-INF**) folder and choose **New**, then **JSP File**. Name the file `mail.jsp` and click **Finish**.  Replace the entire contents of `mail.jsp` with the following:

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${error ne null}">
	<div class="alert alert-danger">Error: ${error}</div>
</c:if>

<pre><code>Auth code: ${authCode}

ID token: ${idToken}</code></pre>
```

For now, the view is simply going to show us the authorization code, so we can confirm that our sign-in works. We'll update this later once we add more functionality to our app.

Open the `pages.xml` file (**Deployed Resources**, **webapp**, **WEB-INF**, **defs**) and add the following code to the bottom of the file, *before* the `</tiles-definitions>` line:

```xml
<definition name="mail" extends="common">
  <put-attribute name="title" value="My Mail" />
  <put-attribute name="body" value="/WEB-INF/jsp/mail.jsp" />
  <put-attribute name="current" value="mail" />
</definition>
```

Save all of your changes and restart the app. Browse to http://localhost:8080 and click the login button. Sign in with an Office 365 or Outlook.com account. Once you sign in and grant access to your information, the browser should redirect to the app, which displays the authorization code.

![](./readme-images/auth-code.PNG)

### Exchanging the code for a token ###

## Using the Mail API ##

## Next Steps ##

Now that you've created a working sample, you may want to learn more about the [capabilities of the Mail API](https://msdn.microsoft.com/office/office365/APi/mail-rest-operations). If your sample isn't working, and you want to compare, you can download the end result of this tutorial from [GitHub](https://github.com/jasonjoh/java-tutorial). If you download the project from GitHub, be sure to put your application ID and secret into the code before trying it.

## Copyright ##

Copyright (c) Microsoft. All rights reserved.

----------
Connect with me on Twitter [@JasonJohMSFT](https://twitter.com/JasonJohMSFT)

Follow the [Outlook/Exchange Dev Blog](https://blogs.msdn.microsoft.com/exchangedev/)