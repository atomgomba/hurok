package com.ekezet.hurok

interface DependencyContainer {
    operator fun plus(dependency: Any)
}
