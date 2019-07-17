FROM jenkins/jenkins:lts

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

# Copy groovy scripts
COPY groovy/config-maven.groovy /usr/share/jenkins/ref/init.groovy.d/
COPY groovy/harden-jenkins.groovy /usr/share/jenkins/ref/init.groovy.d/
COPY groovy/default-user.groovy /usr/share/jenkins/ref/init.groovy.d/
COPY groovy/set-global-credentials.groovy /usr/share/jenkins/ref/init.groovy.d/

# Set Jenkins User and Password
ENV JENKINS_USER jenkins-admin
ENV JENKINS_PASS jenkins-admin-password

# Install Jenkins plugins
COPY plugins.txt /usr/share/jenkins/ref/
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
