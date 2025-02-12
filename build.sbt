import xerial.sbt.Sonatype.GitHubHosting

name := "spark-sinfony"

version := "0.2"

scalaVersion := "2.11.12"
enablePlugins(GitBranchPrompt)
git.gitTagToVersionNumber := { tag: String =>
  if(tag matches "v[0-9]+\\..*") Some(tag)
  else None
}

val sparkVersion = "2.4.4"

libraryDependencies ++= Seq(
  "org.apache.spark"        %%  "spark-core"                    % sparkVersion,
  "com.b2wdigital.iafront"  %%  "simple-command-line-parser"    % "1.3",
  "net.jcazevedo"           %%  "moultingyaml"                  % "0.4.1",
  "org.scalatest"           %%  "scalatest"                     % "3.0.8"       % "test"
)

/// Configurações para execução
run in Compile :=
  Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)).evaluated
runMain in Compile := Defaults.runMainTask(fullClasspath in Compile, runner in(Compile, run)).evaluated

/// Gestão de conflitos
assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
  case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
  case PathList("com", "amazonaws", xs @ _*) => MergeStrategy.last
  case PathList("com", "syncron", xs @ _*) => MergeStrategy.first
  case PathList("javax", "inject", xs @ _*) => MergeStrategy.first
  case PathList("org", "aopalliance", xs @ _*) => MergeStrategy.first
  case PathList("org", "fusesource", xs @ _*) => MergeStrategy.first
  case PathList("org", "apache", "arrow", xs @_*) => MergeStrategy.rename
  case "about.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/MANIFEST.MF" => MergeStrategy.discard
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case "mozilla/public-suffix-list.txt" => MergeStrategy.last
  case x =>
    MergeStrategy.first
}

artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.withClassifier(Some("assembly"))
}
addArtifact(artifact in (Compile, assembly), assembly)

// Build do jar
assembleArtifact in assemblyPackageScala := false
assemblyJarName in assembly := s"${name.value}-${version.value}.jar"
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
assemblyOutputPath in assembly := new File(s"deployment/${name.value}-${version.value}.jar")

/// Publishing
publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
publishMavenStyle := true

licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

sonatypeProfileName := "com.b2wdigital"
publishTo := sonatypePublishToBundle.value
sonatypeProjectHosting := Some(GitHubHosting("andreclaudino", "SparkSinfony", ""))

homepage := Some(url("https://github.com/andreclaudino/SparkSinfony"))
scmInfo := Some(
  ScmInfo(url("https://github.com/andreclaudino/SparkSinfony"), "scm:git@github.com:andreclaudino/SparkSinfony.git")
)