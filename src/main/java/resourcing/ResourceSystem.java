package resourcing;

import resourcing.resources.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResourceSystem {
    private Map<String, Resource> resources = new HashMap<>();
    private Map<String, Map<String, String>> configs;
    private static ResourceSystem instance;
    private final String RESOURCES_ROOT = "./resources";

    private ResourceSystem() {

    }

    public static ResourceSystem getInstance() {
        if (instance == null) {
            instance = new ResourceSystem();
            instance.reloadResources();
        }

        return instance;
    }

    public Resource getResource(String fileName) {
        return resources.get(fileName);
    }

    public Map<String, String> getConfig(String fileName) {
        return configs.get(fileName);
    }

    public void reloadResources() {
        VFS vfs = new VFSImpl(RESOURCES_ROOT);
        resources.clear();

        for (String fileName: vfs.iterate(RESOURCES_ROOT)) {
            if (!vfs.isDirectory(fileName)) {
                try {
                    Resource resource = XmlParser.parse(vfs.getUTF8Text(fileName));
                    resources.put(fileName, resource);
                } catch (ParserException | IOException e) {
                    System.err.println("Unable to load resource: " + fileName);
                    e.printStackTrace();
                }
            }
        }
    }
}
