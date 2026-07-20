FROM fedora:36

RUN dnf install -y java-17-openjdk-devel tcpdump procps-ng && dnf clean all

WORKDIR /rmi-lab
COPY src ./src

RUN javac -encoding UTF-8 src/mx/ipn/esimecu/rpc/*.java

# Certificado autofirmado para RMI sobre TLS (reto de extensión)
RUN keytool -genkeypair -alias rmiserver -keyalg RSA -keysize 2048 \
    -dname "CN=esimecu-vm, OU=ESIME Culhuacan, O=IPN, L=CDMX, C=MX" \
    -validity 365 -keystore /rmi-lab/certs.jks -storepass changeit -keypass changeit

ENV RMI_TLS_OPTS="-Djavax.net.ssl.keyStore=/rmi-lab/certs.jks -Djavax.net.ssl.keyStorePassword=changeit -Djavax.net.ssl.trustStore=/rmi-lab/certs.jks -Djavax.net.ssl.trustStorePassword=changeit"

CMD ["/bin/bash"]
