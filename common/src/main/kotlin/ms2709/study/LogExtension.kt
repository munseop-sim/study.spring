package ms2709.study

import org.slf4j.LoggerFactory

/**
 * [kotlin] 지정된 클래스와 연결된 로거 인스턴스 생성
 */
inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!

/**
 * [java] 지정된 클래스와 연결된 로거 인스턴스 생성
 */
fun logger(forClass: Class<*>): org.slf4j.Logger = LoggerFactory.getLogger(forClass)
