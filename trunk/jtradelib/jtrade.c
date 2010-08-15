/*
 ============================================================================
 Name        : startjvm.c
 Author      : liujiang
 Version     :
 Copyright   : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <windows.h>
#include "winbase.h"
#include <io.h>
#include "jni.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "Shlwapi.h"

#define EXPORT __declspec(dllexport)

#define MAXPATHLEN 200
#define _launcher_debug 0
#define JRE_KEY	    "Software\\JavaSoft\\Java Runtime Environment"

static jobject objTradeService;
static JNIEnv *env;
static JavaVM *jvm;
static HINSTANCE hInstance;
static jmethodID runMethod = NULL;
static int jvmState = -1;

typedef jint(JNICALL *CreateJavaVM_t)
(JavaVM **pvm, void **env, void *args);

static jstring newJavaString(JNIEnv *env, char * str);
static char* JNI_GetStringChars(JNIEnv *env, jstring str);
static jboolean GetStringFromRegistry(HKEY key, const char *name, char *buf,
		jint bufsize);
static jboolean GetPublicJREHome(char *buf, jint bufsize);
#pragma   comment(lib,   "shlwapi.lib.")

EXPORT void __stdcall showMsg(char* msg) {
	MessageBox(0, msg, "Hi", MB_ICONINFORMATION);
}

EXPORT int __stdcall startJavaVM() {

	//定义一个函数指针，下面用来指向JVM中的JNI_CreateJavaVM函数
	typedef jint (JNICALL *PFunCreateJavaVM)(JavaVM **, void **, void *);
	int res;

	jint bsize = MAXPATHLEN;
	char jre_path[MAXPATHLEN];
	char tmpbuf[MAXPATHLEN];
	CreateJavaVM_t funCreateJavaVM;
	jclass javaClass;

	JavaVMInitArgs vm_args;
	JavaVMOption options[3];

	options[0].optionString = "-Djava.compiler=NONE";
	options[1].optionString
			= "-Djava.class.path=.;D:\\java_workspace\\mt4\\bin";
	options[2].optionString = "-verbose:NONE";

	vm_args.version = JNI_VERSION_1_4;
	vm_args.nOptions = 3;
	vm_args.options = options;
	vm_args.ignoreUnrecognized = JNI_TRUE;

	GetPublicJREHome(jre_path, bsize);
	//加载JVM.DLL动态库

	strcpy_s(tmpbuf, MAXPATHLEN, jre_path);
	strcat_s(tmpbuf, MAXPATHLEN, "\\bin\\server\\jvm.dll");
	if ((PathFileExists(tmpbuf)) != 1) {
		strcpy_s(tmpbuf, MAXPATHLEN, jre_path);
		strcat_s(tmpbuf, MAXPATHLEN, "\\bin\\client\\jvm.dll");
		if ((PathFileExists(tmpbuf)) != 1) {
			fprintf(stderr, "Load jvm.dll faild! Path:%s\n", tmpbuf);
			return -3;
		}
	}
	hInstance = LoadLibrary(tmpbuf);
	if (hInstance == NULL) {
		fprintf(stderr, "load jvm.dll faild! Path:%s\n", tmpbuf);
		return -2;
	}

	funCreateJavaVM = (CreateJavaVM_t) GetProcAddress(hInstance,
			"JNI_CreateJavaVM");
	//取得里面的JNI_CreateJavaVM函数指针

	//调用JNI_CreateJavaVM创建虚拟机
	res = (*funCreateJavaVM)(&jvm, (void**) &env, &vm_args);
	if (res < 0) {
		puts("JVM create failed!\n");
		return res;
	}
	//查找test.Demo类，返回JAVA类的CLASS对象
	javaClass = (*env)->FindClass(env, "forex/auto/trade/core/TradeMain");
	if (javaClass != NULL) {
		//根据类的CLASS对象获取该类的实例

		jmethodID mainConstructor = (*env)->GetMethodID(env, javaClass,
				"<init>", "()V");

		objTradeService = (*env)->NewObject(env, javaClass, mainConstructor);

		//获取类中的方法，最后一个参数是方法的签名，通过javap -s -p 文件名可以获得


		jmethodID startMethod = NULL;
		startMethod = (*env)->GetMethodID(env, javaClass, "start", "()V");
		if (startMethod != NULL) {

			(*env)->CallStaticVoidMethod(env, javaClass, startMethod);
			showMsg("call start finished!");
		}

		runMethod = (*env)->GetMethodID(env, javaClass, "syncData", "(JDDDD)I");
		if (runMethod == NULL) {
			puts("JVM create failed! javaClass not found.\n");
		}

	} else {
		puts("JVM create failed! javaClass not found.\n");
	}

	jvmState = 0;
	showMsg("JVM started");
	return 0;
}

EXPORT void __stdcall cleanupVM(int exitCode) {

	jclass systemClass = NULL;
	jmethodID exitMethod = NULL;
	JNIEnv * localEnv = env;
	(*jvm)->AttachCurrentThread(jvm, (void**) &localEnv, NULL);

	(*env)->DeleteLocalRef(env, objTradeService);
	systemClass = (*env)->FindClass(env, "forex/auto/trade/core/TradeMain");
	if (systemClass != NULL) {
		exitMethod = (*env)->GetStaticMethodID(env, systemClass, "stop", "()V");
		if (exitMethod != NULL) {

			(*env)->CallStaticVoidMethod(env, systemClass, exitMethod);
			showMsg("call exit finished!");
		}

	}
	if ((*env)->ExceptionOccurred(env)) {
		(*env)->ExceptionDescribe(env);
		(*env)->ExceptionClear(env);
	}

	(*jvm)->DestroyJavaVM(jvm);
	jvmState = -1;
	puts("End call java."); // prints !!!Hello World!!!

}

EXPORT void __stdcall doTrade(double ask) {
	int cmd = 0;
	JNIEnv * localEnv = env;
	if (jvmState != 0) {
		showMsg("JVM is not start!\n");
		return;
	}

	if (runMethod != NULL) {
		jstring msg;
		char* ret;
		(*jvm)->AttachCurrentThread(jvm, (void**) &localEnv, NULL);
		//构造参数并调用对象的方法
		//jstring arg = newJavaString(env, szTest);

		msg = (jstring)(*localEnv)->CallObjectMethod(localEnv, objTradeService,
				runMethod, ask);
		ret = (char*) JNI_GetStringChars(localEnv, msg);
		(*localEnv)->DeleteLocalRef(localEnv, msg);
		showMsg(ret);
		(*jvm)->DetachCurrentThread(jvm);

	} else {
		showMsg("run Method is null");
	}
}

EXPORT void __stdcall syncData(long time, double open, double high, double low,
		double close) {
	int cmd = 0;
	JNIEnv * localEnv = env;
	if (jvmState != 0) {
		showMsg("JVM is not start!\n");
		return;
	}

	if (runMethod != NULL) {
		jstring msg;
		char* ret;
		(*jvm)->AttachCurrentThread(jvm, (void**) &localEnv, NULL);
		//构造参数并调用对象的方法
		//jstring arg = newJavaString(env, szTest);

		msg = (jstring)(*localEnv)->CallObjectMethod(localEnv, objTradeService,
				runMethod, ask);
		ret = (char*) JNI_GetStringChars(localEnv, msg);
		(*localEnv)->DeleteLocalRef(localEnv, msg);
		showMsg(ret);
		(*jvm)->DetachCurrentThread(jvm);

	} else {
		showMsg("run Method is null");
	}
}

static jstring newJavaString(JNIEnv *env, char* str) {
	jstring newString = NULL;
	int length = strlen(str);
	jbyteArray bytes = (*env)->NewByteArray(env, length);
	if (bytes != NULL) {
		(*env)->SetByteArrayRegion(env, bytes, 0, length, str);
		if (!(*env)->ExceptionOccurred(env)) {
			jclass stringClass = (*env)->FindClass(env, "java/lang/String");
			if (stringClass != NULL) {
				jmethodID ctor = (*env)->GetMethodID(env, stringClass,
						"<init>", "([B)V");
				if (ctor != NULL) {
					newString
							= (*env)->NewObject(env, stringClass, ctor, bytes);
				}
			}
		}
		(*env)->DeleteLocalRef(env, bytes);
	}

	if (newString == NULL) {
		(*env)->ExceptionDescribe(env);
		(*env)->ExceptionClear(env);
	}
	return newString;
}

/* Get a _TCHAR* from a jstring, string should be released later with JNI_ReleaseStringChars */
static char* JNI_GetStringChars(JNIEnv *env, jstring str) {

	char* result = NULL;
	/* Other platforms, use java's default encoding */
	char* buffer = NULL;
	jclass stringClass = (*env)->FindClass(env, "java/lang/String");
	if (stringClass != NULL) {
		jmethodID getBytesMethod = (*env)->GetMethodID(env, stringClass,
				"getBytes", "()[B");
		if (getBytesMethod != NULL) {
			jbyteArray bytes = (*env)->CallObjectMethod(env, str,
					getBytesMethod);
			if (!(*env)->ExceptionOccurred(env)) {
				jsize length = (*env)->GetArrayLength(env, bytes);
				buffer = malloc((length + 1) * sizeof(char*));
				(*env)->GetByteArrayRegion(env, bytes, 0, length,
						(jbyte*) buffer);
				buffer[length] = 0;
			}
			(*env)->DeleteLocalRef(env, bytes);
		}
	}
	if (buffer == NULL) {
		(*env)->ExceptionDescribe(env);
		(*env)->ExceptionClear(env);
	}
	result = buffer;
	return result;
}

static jboolean GetStringFromRegistry(HKEY key, const char *name, char *buf,
		jint bufsize) {
	DWORD type, size;

	if (RegQueryValueEx(key, name, 0, &type, 0, &size) == 0 && type == REG_SZ
			&& (size < (unsigned int) bufsize)) {
		if (RegQueryValueEx(key, name, 0, 0, buf, &size) == 0) {
			return JNI_TRUE;
		}
	}
	return JNI_FALSE;
}

static jboolean GetPublicJREHome(char *buf, jint bufsize) {
	HKEY key, subkey;
	char version[MAXPATHLEN];

	/*
	 * Note: There is a very similar implementation of the following
	 * registry reading code in the Windows java control panel (javacp.cpl).
	 * If there are bugs here, a similar bug probably exists there.  Hence,
	 * changes here require inspection there.
	 */

	/* Find the current version of the JRE */
	if (RegOpenKeyEx(HKEY_LOCAL_MACHINE, JRE_KEY, 0, KEY_READ, &key) != 0) {
		fprintf(stderr, "Error opening registry key '" JRE_KEY "'\n");
		return JNI_FALSE;
	}

	if (!GetStringFromRegistry(key, "CurrentVersion", version, sizeof(version))) {
		fprintf(stderr, "Failed reading value of registry key:\n\t"
		JRE_KEY "\\CurrentVersion\n");
		RegCloseKey(key);
		return JNI_FALSE;
	}

	//	if (strcmp(version, DOTRELEASE) != 0) {
	//		fprintf(stderr, "Registry key '" JRE_KEY "\\CurrentVersion'\nhas "
	//				"value '%s', but '" DOTRELEASE "' is required.\n", version);
	//		RegCloseKey(key);
	//		return JNI_FALSE;
	//	}

	/* Find directory where the current version is installed. */
	if (RegOpenKeyEx(key, version, 0, KEY_READ, &subkey) != 0) {
		fprintf(stderr, "Error opening registry key '"
		JRE_KEY "\\%s'\n", version);
		RegCloseKey(key);
		return JNI_FALSE;
	}

	if (!GetStringFromRegistry(subkey, "JavaHome", buf, bufsize)) {
		fprintf(stderr, "Failed reading value of registry key:\n\t"
		JRE_KEY "\\%s\\JavaHome\n", version);
		RegCloseKey(key);
		RegCloseKey(subkey);
		return JNI_FALSE;
	}

	if (_launcher_debug) {
		char micro[MAXPATHLEN];
		if (!GetStringFromRegistry(subkey, "MicroVersion", micro, sizeof(micro))) {
			printf("Warning: Can't read MicroVersion\n");
			micro[0] = '\0';
		}
		printf("Version major.minor.micro = %s.%s\n", version, micro);
	}

	RegCloseKey(key);
	RegCloseKey(subkey);
	return JNI_TRUE;
}

