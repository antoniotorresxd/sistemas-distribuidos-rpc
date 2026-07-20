FROM fedora:36

RUN dnf install -y nginx fcgiwrap curl jq java-17-openjdk-devel tcpdump procps-ng && dnf clean all

RUN mkdir -p /usr/lib/cgi-bin/rpc
COPY rpc/cotizar.cgi /usr/lib/cgi-bin/rpc/cotizar.cgi
RUN chmod +x /usr/lib/cgi-bin/rpc/*.cgi

COPY nginx/rpc-gateway.conf /etc/nginx/conf.d/rpc-gateway.conf
RUN rm -f /etc/nginx/conf.d/default.conf

WORKDIR /app
COPY src ./src
RUN javac -encoding UTF-8 -d out src/mx/ipn/esimecu/rpc/*.java

EXPOSE 8080

CMD ["/bin/bash", "-c", \
  "java -cp /app/out mx.ipn.esimecu.rpc.ServicioSaldoJVM > /tmp/saldo-jvm.log 2>&1 & \
   fcgiwrap -s unix:/run/fcgiwrap.socket -f & \
   sleep 1 && chmod 666 /run/fcgiwrap.socket && nginx -g 'daemon off;'"]
