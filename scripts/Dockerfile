FROM ubuntu:18.04

RUN export DEBIAN_FRONTEND=noninteractive \
&& apt-get -qqy update \
&& apt-get -qqy install \
    openjdk-8-jdk \
    wget \
    unzip \
    git \
    python-pip \
    python2.7 \
    python3-pip \
    python3 \
&& pip2 install pyyaml \
&& apt-get clean \
&& rm -rf /var/lib/apt/lists/*

# Download and untar Android SDK tools
RUN mkdir -p /usr/local/android-sdk-linux \
&& wget https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip -O sdk-tools.zip \
&& unzip sdk-tools.zip -d /usr/local/android-sdk-linux \
&& rm sdk-tools.zip

# Set environment variable
ENV ANDROID_HOME /usr/local/android-sdk-linux
ENV PATH ${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/tools/bin:${ANDROID_HOME}/platform-tools

# Accept licenses before installing components
RUN yes | sdkmanager --licenses

# Update and install SDK Manager
# The `yes` is for accepting all non-standard tool licenses.
RUN yes | sdkmanager --update --channel=3
RUN yes | sdkmanager \
    "platforms;android-29" \
    "platforms;android-28" \
    "build-tools;29.0.2" \
    "build-tools;29.0.1" \
    "build-tools;29.0.0" \
    "build-tools;28.0.3" \
    "build-tools;28.0.2" \
    "build-tools;28.0.1" \
    "build-tools;28.0.0" \
    "extras;android;m2repository" \
    "extras;google;m2repository" \
    "cmake;3.10.2.4988404"
