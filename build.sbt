organization := "com.scet"

name := "play-authenticate-hibernate"

scalaVersion := "2.11.7"

version := "1.0-SNAPSHOT"

PlayKeys.externalizeResources := true

val appDependencies = Seq(
  javaJpa,
  "be.objectify"  %% "deadbolt-java"     % "2.5.0",
  // Comment the next line for local development of the Play Authentication core:
  "com.feth"      %% "play-authenticate" % "0.8.1",
  //"org.postgresql"    %  "postgresql"        % "9.4-1201-jdbc41",
  "org.hibernate" % "hibernate-entitymanager" % "5.2.7.Final",
  "mysql" % "mysql-connector-java" % "5.1.40",
  javaJdbc,
  cache,
  javaWs,
  "org.webjars" % "bootstrap" % "3.2.0",
  "org.easytesting" % "fest-assert" % "1.4" % "test",
  "dom4j" % "dom4j" % "1.6.1"
)

// add resolver for deadbolt and easymail snapshots
resolvers += Resolver.sonatypeRepo("snapshots")

routesGenerator := InjectedRoutesGenerator

EclipseKeys.preTasks := Seq(compile in Compile)

//  Uncomment the next line for local development of the Play Authenticate core:
//lazy val playAuthenticate = project.in(file("modules/play-authenticate")).enablePlugins(PlayJava)

lazy val root = project.in(file(".")).enablePlugins(PlayJava).settings(libraryDependencies ++= appDependencies)

  /* Uncomment the next lines for local development of the Play Authenticate core: */
  //.dependsOn(playAuthenticate)
  //.aggregate(playAuthenticate)
