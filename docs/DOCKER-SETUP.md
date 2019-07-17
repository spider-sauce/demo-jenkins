# Jenkins + Sauce OnDemand Docker Deployment Guide

**Deployment Options:**
1. [Configure Sauce OnDemand in a Jenkins Docker Container](#configure-sauce-ondemand-in-a-jenkins-docker-container)
    1. [Install Docker](#install-docker)
    2. [Download and Run Jenkins in Docker](#download-and-run-jenkins-on-docker)
    3. [Configure Jenkins](#configure-jenkins)
    4. [Install Sauce OnDemand Plugin](#install-sauce-ondemand-plugin)
    5. [Configure the Sauce OnDemand Plugin](#configure-the-sauce-ondemand-plugin)
    6. [Test the SauceLabs OnDemand Jenkins Plugin](#test-the-saucelabs-ondemand-jenkins-plugin)
2. [Build a Custom Jenkins Sauce OnDemand Docker Image](#build-a-custom-jenkins-sauce-ondemand-docker-image)
    1. [Commit and Tag the Docker Image](#commit-and-tag-the-docker-image)
    2. [Run the Container](#run-the-container)
3. [Deploying Jenkins and Sauce On Demand from a `Dockerfile`](#deploying-jenkins-and-sauce-on-demand-from-a-dockerfile)
    1. [Create the `Dockerfile`](#create-the-dockerfile)
    2. [Build and Tag the Docker Image](#build-and-tag-the-docker-image)
    3. [Run the New Docker Container](#run-the-new-docker-container)


Resources: [Helpful Docker Commands](#helpful-docker-commands)
    
## Configure Sauce OnDemand in a Jenkins Docker Container

If you plan on combining this workflow with other service-oriented toolsets, you may want to experiment deploying Jenkins using Docker. 

For a detailed explanation follow the instructions in the Jenkins documentation:[https://jenkins.io/doc/book/installing/#downloading-and-running-jenkins-in-docker](https://jenkins.io/doc/book/installing/#downloading-and-running-jenkins-in-docker)

### Install Docker

[Docker](https://www.docker.com/why-docker) is a containerization platform used to build and control application deployment. 
In this example we use Docker to setup a local jenkins server and then expose a container port of said server

#### MacOSX:

1. Go to [https://docs.docker.com/docker-for-mac/install/](https://docs.docker.com/docker-for-mac/install/).
2. Click **Download from Docker Hub**.
3. When the download completes, double-click the `.dmg` file open the installer package.
4. Double-click the installer package to begin the installation.
5. Continue through the installation prompts.
6. Sign up for [Docker Hub](https://hub.docker.com/) in order to pull down public docker images.
7. After signing up for Docker Hub, launch the Docker application and set your login credentials

#### Windows:

1. Go to [https://docs.docker.com/docker-for-windows/install/](https://docs.docker.com/docker-for-windows/install/)
2. Click **Download from Docker Hub**.
3. When the download completes, double-click the `.exe` file open the installer package.
4. Follow the steps in the setup wizard to complete the installation. You should accept all the default settings.
5. Continue through the installation prompts.
6. Sign up for [Docker Hub](https://hub.docker.com/) in order to pull down public docker images.
7. After signing up for Docker Hub, launch the Docker application and set your login credentials
<br />


### Download and Run Jenkins on Docker

#### MacOSX:
1. Open up a terminal window
2. Download the **`jenkinsci/blueocean`** image and run it as a container with the following `docker` cli commands:
```
docker run \
--name jenkins-sauce \
-u root \
--rm \
-d \
-p 8080:8080 \
-p 50000:50000 \
-v jenkins-data:/var/jenkins_home \
-v /var/run/docker.sock:/var/run/docker.sock \
jenkinsci/blueocean
```
3. Explanation: the **`-p`** flag sets **`localhost:containerport`** mappings. In other words, **`8080:8080`** implies that the Jenkins server listens on its internal container port of **`8080`**, and the container itself listens on the localhost port **`8080`**.

#### Windows:
1. Open a command prompt window.
2. Download the **`jenkinsci/blueocean`** image and run it as a container with the following `docker` cli commands:
```
docker run ^
--name jenkins-sauce ^
-u root ^
--rm ^
-d ^
-p 8080:8080 ^
-p 50000:50000 ^
-v jenkins-data:/var/jenkins_home ^
-v /var/run/docker.sock:/var/run/docker.sock ^
jenkinsci/blueocean
```
3. Explanation: the **`-p`** flag sets **`localhost:containerport`** mappings. In other words, **`8080:8080`** implies that the Jenkins server listens on its internal container port of **`8080`**, and the container itself listens on the localhost port **`8080`**.

### Configure Jenkins
1. Open a local browser on your machine and navigate to `http://localhost:8080`
2. You should see a message appear asking for an administrator password. In order to retrieve the password run the following command to `ssh` into the docker container:
```
docker exec -it jenkins-sauce bash
```
3.Run the following command to retrieve the password.
```
cat /var/jenkins_home/secrets/initialAdminPassword
#Example Output
fcbd9c170eb94a189f631c6c18e941df
```
4. Copy and paste the password into the browser, then follow the installation prompts—including the recommended Jenkins plugin installation.
5. Create you first Admin User

### Install Sauce OnDemand Plugin
1. On your Jenkins Administration page, click **Manage Jenkins** 
2. Click **Manage Plugins** 
3. Click the **Available** tab. 
4. In the list of plugins, find and select **Sauce OnDemand Plugin**. 
5. Click **Download now and Install after restart**.
    > The plugin file is fairly large, so the download may take several minutes.  
6. In the plugin installation dialog, select **Restart Jenkins** when installation is complete and no jobs are running. 

### Configure the Sauce OnDemand Plugin
In order to use the Sauce OnDemand Plugin, you must authenticate your SauceLabs account. 

The plugin provides an interface for storing your SauceLabs auth credentials as environment variables on the Jenkins Server so that you don't need to hard code the values.

1. Restart Jenkins and go to your **Administration** Page.
2. On the left-hand side, click **Credentials**.
3. You can select an existing domain, or select **Add domain**.
    > If you don't see these options it means you are not a Jenkins Admin. Confirm you have admin privileges before continuing.
4. Select the domain of your choice, and then select **Add Credentials**.
5. Under **Kind**, select **Sauce Labs**.
6. Enter your Sauce Labs **Username** and **Access Key**
    > If you're not sure how to access your SauceLabs credentials go to the [saucelabs.com](https://app.saucelabs.com/dashboard/builds) **Dashboard**, select the arrow next to your account name, and select **User Settings**. Then copy the information to your local clipboard.
7. Click **OK** to save the credentials.

### Test the SauceLabs OnDemand Jenkins Plugin
1. Open the Jenkins Dashboard
2. Select **Create New Jobs** or **Create New Item**
3. Select **Freestyle Project**
3. Name the job `jenkins-saucelabs-github-test` and select **OK**
4. In the job configuration, select **Source Code Management**
5. Select the **Git** radial button
    > If the radial button doesn't appear, try re-installing the plugins followed by a restart and re-login to your Jenkins dashboard.
6. Enter the Git repository URL to pull from GitHub
    > You may receive an error message the first time you enter a repo URL. This generally happens if you don't have **`git`** installed on your local machine.
7. If you're repository is private enter your GitHub credentials in the **Credentials** section, then click **Add**.
    > If you've enabled 2 factor auth for your GitHub Account, add an SSH Key specific to your Jenkins instance by following the [instructions here](https://help.github.com/en/articles/connecting-to-github-with-ssh) or [here](https://wiki.jenkins.io/display/JENKINS/SSH+Credentials+Plugin)
8. Save the configuration.
    > Note: it's best to ensure the repo you've set in **Source Code Management** has a legitimate Selenium test, if you're unsure feel free to use [one of our examples](https://github.com/saucelabs-sample-test-frameworks/sample-Jenkins-pipeline) in our sample frameworks GitHub organization.
9. Return to the dashboard and run the job. Monitor the dashboard for any errors thrown, if not then you've successfully configured the Jenkins + Github integration.

## Build a Custom Jenkins Sauce OnDemand Docker Image

Once you've successfully tested your Jenkins and Sauce OnDemand integration, it's time to build the docker image.

### Commit and Tag the Docker Image
1. Open a terminal or command prompt on your local machine.
2. Run the following command to `commit` and `tag` the docker image from a running container:
```
docker commit jenkins-sauce jenkins-on-demand-sauce
```
> If you haven't configured Sauce OnDemand with a Jenkins container, please [refer to this guide]().
3. Confirm that the new docker image exists by running the following command:
```
docker images
```
4. Delete the original container:
```
docker stop jenkins-sauce
docker rm jenkins-sauce
``` 
5. Confirm that there are no running containers:
```
docker ps
```

### Run the Container
1. Create a new container based on your new tagged docker image:
```
docker run --name jenkins-test -d -p 8080:8080 jenkins-on-demand-sauce
```
2. In a local browser, navigate to `http://localhost:8080` and confirm that the previous plugin installation and jenkins configurations still exist.


## Deploying Jenkins and Sauce On Demand from a `Dockerfile`

After [creating a custom docker image](), the next step is to template this existing setup through the use of a docker manifest file, also known as a `DOCKERFILE`.

### Create the `Dockerfile`
1. Create a `Dockerfile` with the following:
    ```
    FROM jenkins/jenkins:lts
    
    # Install Jenkins plugins
    COPY plugins.txt /usr/share/jenkins/ref/
    RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

    ```
### Build and Tag the Docker Image
1. Run the following command to commit and tag the new docker image from the `Dockerfile`:
```
docker build -t jenkins/sauce-on-demand:latest .
```
2. Verify that the image was created:
```
docker images
# Example Output
REPOSITORY                TAG                 IMAGE ID            
jenkins/sauce-on-demand   latest              834bdca7a80b
jenkins/jenkins           lts                 95bf220e341a
```
> You can optionally tag and push the image to an external repository like dockerhub.com. For more information [read the docker documentation](https://docs.docker.com/engine/reference/commandline/push/).

### Run the New Docker Container
1. Run a docker container based on the newly built image:
```
docker run \
--name jenkins-sauce \
-u root \
--rm \
-d \
-p 8080:8080 \
-p 50000:50000 \
-v jenkins-data:/var/jenkins_home \
-v /var/run/docker.sock:/var/run/docker.sock \
jenkins/sauce-on-demand
```
> If you receive the following error: 
```
docker: Error response from daemon: pull access denied for jenkins/sauce-on-demand, repository does not exist or may require 'docker login'
```
> It most likely means you didn't create a custom docker image and you need to start over from the `docker build` command.

5. Enter the following command to `ssh` into the docker container:
```
docker exec -it jenkins-sauce bash
```
6. Run the following command to retrieve the password.
```
cat /var/jenkins_home/secrets/initialAdminPassword
#Example Output
fcbd9c170eb94a189f631c6c18e941df
```
7. Copy and paste the password into the browser, then follow the installation prompts—including the recommended Jenkins plugin installation.
8. Navigate to `http://localhost:8080` and step through the remainder of Jenkins installation.
9. Configure your Sauce Labs Credentials in the Jenkins Administration page:

> If you're not sure how to do this, [follow the steps on this guide](https://wiki.saucelabs.com/display/DOCS/Jenkins+and+Sauce+OnDemand+Plugin+Quickstart+Guide)

> If you've installed the `jenkinscli` and the Credentials plugin, you can inject credentials through `$JENKINS_HOME/secrets/credentials.xml`. For more information refer to [this page](https://wiki.jenkins.io/display/JENKINS/Credentials+Plugin) in the Credentials Jenkins plugin documentation.

### Helpful Docker Commands
The following commands will help you clean up any hanging docker containers or images:
#### List all Containers:
```
docker ps -aq
```
#### Stop all Running Containers
```
docker stop $(docker ps -aq)
```
#### Remove all Containers
```
docker rm $(docker ps -aq)
```
#### Remove all Docker Images
```
docker rmi $(docker images -q)
```