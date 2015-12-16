package ru.trendtech.services.versioning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import ru.trendtech.repositories.VersioningRepository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * User: max
 * Date: 1/18/13
 * Time: 10:11 PM
 */
class DatabaseVersionUpdater {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseVersionUpdater.class);

    private Resource[] locations;

    @Autowired
    private DatabaseUpdatesInMethods updatesInMethods;

    @javax.annotation.Resource
    private VersioningRepository versioningRepository;

    private DataSource dataSource;

    public void setLocations(org.springframework.core.io.Resource[] locations) {
        this.locations = locations;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @PostConstruct
    public void applyUpdatesToDatabase() {
        Assert.notNull(dataSource, "Data source can't be null");

        DatabaseSystemInfo systemInfo;
        List<DatabaseSystemInfo> dbVersionInfo = versioningRepository.findAll();
        if (!dbVersionInfo.isEmpty()) {
            systemInfo = dbVersionInfo.get(0);
        } else {
            systemInfo = new DatabaseSystemInfo();
        }

        try {
            List<String> fileNames = updateScripts(systemInfo, locations);
            systemInfo.addAppliedScripts(fileNames);

            List<String> methodNames = updateMethods(systemInfo);
            systemInfo.addAppliedMethods(methodNames);

            versioningRepository.save(systemInfo);
        } catch (Exception e) {
            LOGGER.error("Problem on updating Database!!! Application should be stopped!!!", e);
        }

    }

    private List<String> updateMethods(final DatabaseSystemInfo systemInfo) {
        final ArrayList<String> methodNames = new ArrayList<>();
        ReflectionUtils.doWithMethods(DatabaseUpdatesInMethods.class, new ReflectionUtils.MethodCallback() {
                    @Override
                    public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                        ReflectionUtils.invokeMethod(method, updatesInMethods);
                        methodNames.add(method.getName());
                    }
                }, new ReflectionUtils.MethodFilter() {
                    @Override
                    public boolean matches(Method method) {
                        boolean correctMethodName = method.getName().startsWith("update");
                        boolean alreadyUpdated =
                                systemInfo.getAppliedMethods() != null &&
                                        systemInfo.getAppliedMethods().contains(method.getName());
                        return correctMethodName && !alreadyUpdated;
                    }
                }
        );
        return methodNames;
    }

    private List<String> updateScripts(DatabaseSystemInfo systemInfo, Resource[] locations) {
        ArrayList<String> fileNames = new ArrayList<>();
        ArrayList<Resource> newResources = new ArrayList<>();
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        String appliedScripts = "";
        if (systemInfo.getAppliedScripts() != null) {
            appliedScripts = systemInfo.getAppliedScripts();
        }
        for (Resource resource : locations) {
            if (systemInfo.getAppliedScripts() == null || !appliedScripts.contains(resource.getFilename())) {
                newResources.add(resource);
                fileNames.add(resource.getFilename());
            }
        }
        for (Resource resource : newResources) {
            databasePopulator.addScript(resource);
        }
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
        return fileNames;
    }

    private List<String> locationToFileNames(Resource[] locations) {
        ArrayList<String> list = new ArrayList<>();
        for (Resource location : locations) {
            list.add(location.getFilename());
        }
        return list;
    }
}
