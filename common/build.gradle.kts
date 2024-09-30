architectury {
    common("forge", "fabric")
    platformSetupLoomIde()
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${project.properties["fabric_loader_version"]}")
}
