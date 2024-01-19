template {
    source = "haproxy.ctmpl"
    destination = "/etc/haproxy/haproxy.cfg"
    command = "haproxy restart"
}
