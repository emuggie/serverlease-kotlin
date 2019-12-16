package org.emuggie.onion

import org.emuggie.onion.handler.RequestHandler
import org.emuggie.onion.handler.ThreadScoped
import java.lang.Exception
import java.util.*

var handlerStack by ThreadScoped<Stack<RequestHandler>>()

class OnionApp {
    private val basePkg :String

    constructor(basePkg: String){
        this.basePkg = basePkg
    }

//    fun addHandler(handler: RequestHandler):OnionApp{
//        return this
//    }

    /**
     *Handle request based on class
     */
    fun <T>handle(clazz:Class<in T>, method:String) where T : RequestHandler {
        if(!clazz.`package`.name.startsWith(this.basePkg + ".")){
            throw Exception("$clazz Is not subpackage of ${this.basePkg}")
        }
        val clazzMethod = clazz.getDeclaredMethod(method.toLowerCase())?:return

        if(handlerStack == null || handlerStack.empty()){
            handlerStack = Stack()
        }

        val pkgs = clazz.`package`.name.replace(this.basePkg+".","").split(".")
        pkgs.forEachIndexed loop@{index, _ ->
            try {
                println("try ${this.basePkg + "." + toPkgStr(pkgs.subList(0, index + 1))}")
                val handlerClazz =
                    this::class.java.classLoader.loadClass(this.basePkg + "." + toPkgStr(pkgs.subList(0, index + 1)))
                if(handlerStack.any { requestHandler -> handlerClazz == requestHandler.javaClass}){
                    //Already processed before
                    println("dupl")
                    return@loop
                }
                val handler = handlerClazz.newInstance()
                if(handler !is RequestHandler) return@loop
                println("inster$handler")
                handlerStack.push(handler)
                if(!handler.before()){
                    return
                }
            }catch (e :ClassNotFoundException){
                e.printStackTrace()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        try {
            println(handlerStack.last())
            clazzMethod.invoke(handlerStack.last())
            while (handlerStack.size > 0) {
                handlerStack.pop().after()
            }
        }catch (e:Exception){
            e.printStackTrace()
            throw e
        }
    }

    /**
     *
     */
    fun handle(path:String, method:String){
        var dirs = path.split("/")
        println("Path:${path}, Package : ${this.basePkg + "." + toPkgStr(dirs)}")
        val targetClazz = this::class.java.classLoader.loadClass(this.basePkg + "." + toPkgStr(dirs)) ?: return
        if(!RequestHandler::class.java.isAssignableFrom(targetClazz)){
            return
        }
        this.handle(targetClazz ,method)
    }

    private fun toPkgStr(dirs:List<String>):String{
        var pkgStr=""
        dirs.forEachIndexed it@ {
                index, s ->
            if(s =="") return@it
            pkgStr += if(index == dirs.lastIndex){
                s.capitalize()
            }else{
                "$s."
            }
        }
        return pkgStr
    }
}
