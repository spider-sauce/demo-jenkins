# Sauce Labs Jenkins + Sauce OnDemand Ansible Deployment Demo
This repository contains source code examples along with other manifest files to quickly get up and running with Jenkins and the Sauce Labs. There are several options for deployment, checkout the [docs](./docs) directory for specific deployment guides.

## Prerequisites
Below is a list of prerequisite software needed to run the **Quickstart Demo**

* Git
* `docker`
* `ansible`
    
If you wish to install Jenkins locally or create your own custom Jenkins `docker` image, please refer to the following guides:
* [Jenkins Quickstart Guide](./docs/JENKINS-SETUP.md)
* [Docker Deployment Guide](./docs/DOCKER-SETUP.md)

<br />

## Quickstart Demo
1. Install [ansible](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html)
    > On Mac OSX you can use homebrew
    ```
    brew install ansible
    ```
1. Clone this repository:
    ```
    https://github.com/saucelabs-training/demo-jenkins.git
    ```
2. Open a bash shell or command prompt and navigate to the project directory.
3. Open the file `jenkins-docker-build-and-deploy.yml`, and edit the following lines with your [Sauce Labs Credentials](https://wiki.saucelabs.com/display/DOCS/Best+Practice%3A+Use+Environment+Variables+for+Authentication+Credentials):
    ```
    "username":"your-saucelabs-username",
    "apiKey":"your-saucelabs-access-key",
    ```
4. Save the file and run the following commands:
    ```
    ansible-playbook jenkins-docker-build-and-deploy.yml
    ```
5. The output in the console should look like this:
    ```
    ok: [localhost]
    
    TASK [Run Jenkins Docker image] *************************************************************************************************************************************************************************************************************
    ok: [localhost]
    
    TASK [Create Crumb Remote Access Request Field] *********************************************************************************************************************************************************************************************
    ok: [localhost]
    
    TASK [Create Global Sauce Lab Credentials] **************************************************************************************************************************************************************************************************
    ok: [localhost]
    
    PLAY RECAP **********************************************************************************************************************************************************************************************************************************
    localhost                  : ok=4    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0   
    ```
    
6. Open a local browser and navigate to `http://localhost:8080`
7. Follow the steps in the [Testing the Sauce OnDemand Plugin](https://wiki.saucelabs.com/display/DOCS/Jenkins+and+Sauce+OnDemand+Plugin+Quickstart+Guide#test-the-saucelabs-ondemand-jenkins-plugin) to create a job.
8. Run the job and check your [www.saucelabs.com](www.saucelabs.com) dashboard for the test results.

<br />