resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns)
resolvers += Resolver.typesafeRepo("releases")

projectDependencies += "com.oracle.ojdbc" % "ojdbc8" % "19.3.0.0"
projectDependencies += "com.oracle.ojdbc" % "orai18n" % "19.3.0.0"
projectDependencies += "org.flywaydb" % "flyway-core" % "4.2.0"

addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "3.13.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-distributables" % "2.2.0")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.20")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.9.3")