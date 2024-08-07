#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_design_1master_isad_model_provider_ElectroLibAccessProvider_getServerBaseUrl(JNIEnv *env,jobject thiz) {

    // TODO Also update base url in Location Update Service and LoginWithOtpViewModel line 136
    std::string s = "https://app.kifmc.com/";
    return env->NewStringUTF(s.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_design_1master_isad_model_provider_ElectroLibAccessProvider_getMasterKey(JNIEnv *env,jobject thiz) {
    std::string s = "7b227072696d6172794b65794964223a313233343534373238312c226b6579223a5b7b226b657944617461223a7b227479706555726c223a22747970652e676f6f676c65617069732e636f6d2f676f6f676c652e63727970746f2e74696e6b2e41657347636d4b6579222c2276616c7565223a224769426a4669466472623445704a44425a34464262693241494d697973654e53486b4251504d56535551495569673d3d222c226b65794d6174657269616c54797065223a2253594d4d4554524943227d2c22737461747573223a22454e41424c4544222c226b65794964223a313233343534373238312c226f757470757450726566697854797065223a2254494e4b227d5d7d";
    return env->NewStringUTF(s.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_design_1master_isad_model_provider_ElectroLibAccessProvider_getSecretKey(JNIEnv *env,jobject thiz) {
    std::string s = "REY0C72CVJBWGY9SLADKTAUDN14T0KZEARRYMH4ARD4QQKJM3RRJINQF1FU9M9RDBWV9XP";
    return env->NewStringUTF(s.c_str());
}