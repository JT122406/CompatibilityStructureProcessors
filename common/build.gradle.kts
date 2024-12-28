architectury {
    common("forge", "fabric", "neoforge")
    platformSetupLoomIde()
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${project.properties["fabric_loader_version"]}")
}
