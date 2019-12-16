package org.emuggie.onion.app

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import org.emuggie.onion.OnionApp

import org.emuggie.onion.handler.ThreadScoped
import java.net.InetSocketAddress


var request by ThreadScoped<HttpExchange>()

fun main(){
    val app = OnionApp("org.emuggie.onion.app.path")
    val server = HttpServer.create(InetSocketAddress(8080),0)
    val context = server.createContext("/")
    context.setHandler {
            it: HttpExchange ->
        request = it
        app.handle(it.requestURI.toString(),it.requestMethod)
    }
    server.start()
}