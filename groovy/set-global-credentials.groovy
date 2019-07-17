#!groovy

import com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider.FolderCredentialsProperty
import com.cloudbees.hudson.plugins.folder.AbstractFolder
import com.cloudbees.hudson.plugins.folder.Folder
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.plugins.credentials.domains.*
import hudson.plugins.sauce_ondemand.credentials.SauceCredentials

def env = System.getenv()
String id = UUID.randomUUID().toString()
String username = env.SAUCE_USERNAME
String apiKey = env.SAUCE_ACCESS_KEY
String restEndpoint = env.SAUCE_DATA_CENTER


Credentials sauce = new SauceCredentials(CredentialsScope.GLOBAL, id, username, apiKey, restEndpoint, "description")

Jenkins.instance.getAllItems(Folder.class)
    .findAll{it.name.equals('FolderName')}
    .each{
        AbstractFolder<?> folderAbs = AbstractFolder.class.cast(it)
        FolderCredentialsProperty property = folderAbs.getProperties().get(FolderCredentialsProperty.class)
        if(property != null){
            property.getStore().addCredentials(Domain.global(), sauce)
            println property.getCredentials().toString()
        }
    }