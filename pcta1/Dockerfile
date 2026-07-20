FROM fedora:36

RUN dnf install -y nginx fcgiwrap bc curl jq procps-ng && dnf clean all

RUN mkdir -p /usr/lib/cgi-bin/rpc
COPY rpc/saludo.cgi /usr/lib/cgi-bin/rpc/saludo.cgi
COPY rpc/calc.cgi   /usr/lib/cgi-bin/rpc/calc.cgi
RUN chmod +x /usr/lib/cgi-bin/rpc/*.cgi

COPY nginx/rpc-cgi.conf /etc/nginx/conf.d/rpc-cgi.conf
RUN rm -f /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["/bin/bash", "-c", "fcgiwrap -s unix:/run/fcgiwrap.socket -f & sleep 1 && chmod 666 /run/fcgiwrap.socket && nginx -g 'daemon off;'"]
