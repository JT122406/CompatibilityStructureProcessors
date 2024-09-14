architectury {
    common("forge", "fabric")
    platformSetupLoomIde()
}

loom.accessWidenerPath.set(file("src/main/resources/compatprocessors.accesswidener"))

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${project.properties["fabric_loader_version"]}")
}
