
cmake_minimum_required(VERSION 3.4.1)


add_library(
        native-lib

        SHARED

        src/main/cpp/native-lib.cpp)

include_directories(${CMAKE_SOURCE_DIR}/src/main/cpp/include)

# 设置opencv的动态库
add_library(libopencv_java4 SHARED IMPORTED)

set_target_properties(libopencv_java4 PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libopencv_java4.so)

find_library(
        log-lib

        log)


target_link_libraries(
        native-lib libopencv_java4

        ${log-lib})