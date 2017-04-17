#include <jni.h>
#include <string>
#include <stdlib.h>
#include <math.h>

extern "C" {

void Min_Max(const float a,const float b,const float c,float *min_input,float *max_input);

JNIEXPORT  int Java_com_example_administrator_stlload_stlASCiiParser_facas(
        JNIEnv *env,
        jobject obj , jstring filename,jfloatArray n,jfloatArray v) {

    const char *fileRoot = (*env).GetStringUTFChars(filename, NULL);
    char line[256];
    int faces=0;

    FILE *AsciiFile = fopen(fileRoot,"r");
    while (feof(AsciiFile) == 0) {
        fgets(line, sizeof(line), AsciiFile);
        if(strstr(line,"normal")){
            faces++;
        }
    }

    fclose(AsciiFile);
    (*env).ReleaseStringUTFChars(filename, fileRoot);

    return faces;
}

JNIEXPORT void Java_com_example_administrator_stlload_stlASCiiParser_ASCiiParser(
        JNIEnv *env,
        jobject obj, jstring filename,jfloatArray n ,jfloatArray v,jfloatArray xyz,jfloatArray centerxyz) {

    const char *fileRoot = (*env).GetStringUTFChars(filename, NULL);
    float* normal_array = (*env).GetFloatArrayElements(n,NULL);
    float* vertex_array = (*env).GetFloatArrayElements(v,NULL);
    float* vertex_min_max = (*env).GetFloatArrayElements(xyz,NULL);
    float* vertex_centerxyz = (*env).GetFloatArrayElements(centerxyz,NULL);

    float Xmin = 0, Xmax = 0;
    float Ymin = 0, Ymax = 0;
    float Zmin = 0, Zmax = 0;

    float vertexX =0 ,vertexY =0 ,vertexZ =0;
    float normalX =0 ,normalY =0 ,normalZ =0;
    float average_size;

    char line[256];
    char *tokenChar =" \t\n";
    int normal_counter=0 ,vertec_counter=0;

    FILE *AsciiFile = fopen(fileRoot,"r");
    while (feof(AsciiFile) == 0) {
        fgets( line, sizeof(line), AsciiFile );
        if(strstr(line,"normal")){

            strtok(line,tokenChar);
            strtok(NULL,tokenChar);
            /*
            normal_array[normal_counter*3] = atof(strtok(NULL,tokenChar));
            normal_array[normal_counter*3+1] = atof(strtok(NULL,tokenChar));
            normal_array[normal_counter*3+2] = atof(strtok(NULL,tokenChar));
            */

            normalX = atof(strtok(NULL,tokenChar));
            normalY = atof(strtok(NULL,tokenChar));
            normalZ = atof(strtok(NULL,tokenChar));

            normal_array[normal_counter*9]   = normalX;
            normal_array[normal_counter*9+1] = normalY;
            normal_array[normal_counter*9+2] = normalZ;
            normal_array[normal_counter*9+3] = normalX;
            normal_array[normal_counter*9+4] = normalY;
            normal_array[normal_counter*9+5] = normalZ;
            normal_array[normal_counter*9+6] = normalX;
            normal_array[normal_counter*9+7] = normalY;
            normal_array[normal_counter*9+8] = normalZ;

            normal_counter++;

        }
        else if(strstr(line,"vertex")){

            strtok(line," ");
            vertexX = atof(strtok(NULL,tokenChar));    //vertex-X
            if(vertexX > Xmax){Xmax = vertexX;}
            if(vertexX < Xmin){Xmin = vertexX;}
            vertex_array[vertec_counter*3] = vertexX;

            vertexY= atof(strtok(NULL,tokenChar));   //vertex-Y
            if(vertexY > Ymax){Ymax = vertexY;}
            if(vertexY < Ymin){Ymin = vertexY;}
            vertex_array[vertec_counter*3+1] = vertexY;

            vertexZ = atof(strtok(NULL,tokenChar));  //vertex-Z
            if(vertexZ > Zmax){Zmax = vertexZ;}
            if(vertexZ < Zmin){Zmin = vertexZ;}
            vertex_array[vertec_counter*3+2] = vertexZ;

            vertec_counter++;
        }

    }


    average_size = (fabsf(Xmax-Xmin)+fabsf(Ymax-Ymin)+fabsf(Zmax-Zmin))/3;

    for(int i = 0 ; i < vertec_counter*3 ; i++){
        vertex_array[i] = (vertex_array[i]/average_size)*2;
    }


    vertex_min_max[0] = Xmin;
    vertex_min_max[1] = Xmax;
    vertex_min_max[2] = Ymin;
    vertex_min_max[3] = Ymax;
    vertex_min_max[4] = Zmin;
    vertex_min_max[5] = Zmax;


    //vertex_centerxyz[0] = ((Xmin/average_size)*2+(Xmax/average_size)*2)/2.0f;
    //vertex_centerxyz[1] = ((Ymin/average_size)*2+(Ymax/average_size)*2)/2.0f;
    //vertex_centerxyz[2] = ((Zmin/average_size)*2+(Zmax/average_size)*2)/2.0f;

    vertex_centerxyz[0] = (Xmin+Xmax)/average_size;
    vertex_centerxyz[1] = (Ymin+Ymax)/average_size;
    vertex_centerxyz[2] = (Zmin+Zmax)/average_size;

    fclose(AsciiFile);

    (*env).ReleaseStringUTFChars(filename, fileRoot);
    (*env).ReleaseFloatArrayElements(n,normal_array,NULL);
    (*env).ReleaseFloatArrayElements(v,vertex_array,NULL);
    (*env).ReleaseFloatArrayElements(xyz,vertex_min_max,NULL);
    (*env).ReleaseFloatArrayElements(centerxyz,vertex_centerxyz,NULL);
}


    JNIEXPORT int Java_com_example_administrator_stlload_stlBinaryParser_BinaryParser(
            JNIEnv *env,
            jobject obj , jstring filename, jfloatArray nor, jfloatArray ver, jfloatArray xyz,jfloatArray centerxyz) {

        const char *fileRoot = (*env).GetStringUTFChars(filename, NULL);
        float* normal_array = (*env).GetFloatArrayElements(nor,NULL);
        float* vertex_array = (*env).GetFloatArrayElements(ver,NULL);
        float* vertex_min_max = (*env).GetFloatArrayElements(xyz,NULL);
        float* vertex_centerxyz = (*env).GetFloatArrayElements(centerxyz,NULL);

        FILE *binaryFile = fopen(fileRoot,"rb");
        char end[2];
        float ff[12];
        float Xmin = 0, Xmax = 0;
        float Ymin = 0, Ymax = 0;
        float Zmin = 0, Zmax = 0;

        float min_tmp_x=0, max_tmp_x=0;
        float min_tmp_y=0, max_tmp_y=0;
        float min_tmp_z=0, max_tmp_z=0;

        float average_size;

        int i = 0;
        int state = 1;

        fseek(binaryFile,84L,SEEK_SET);

        while(!feof(binaryFile)){

            state = fread(&ff, sizeof(ff),1,binaryFile);  //normal(3)-vertex(9)

            if(state == 0){break;}
            /*
            normal_array[i*3] = ff[0];    //normal-x
            normal_array[i*3+1] = ff[1];  //normal-y
            normal_array[i*3+2] = ff[2];  //normal-z
            */
            normal_array[i*9] = ff[0];
            normal_array[i*9+1] = ff[1];
            normal_array[i*9+2] = ff[2];
            normal_array[i*9+3] = ff[0];
            normal_array[i*9+4] = ff[1];
            normal_array[i*9+5] = ff[2];
            normal_array[i*9+6] = ff[0];
            normal_array[i*9+7] = ff[1];
            normal_array[i*9+8] = ff[2];

            vertex_array[i*9] = ff[3];    //vertex1-x
            vertex_array[i*9+1] = ff[4];  //vertex1-y
            vertex_array[i*9+2] = ff[5];  //vertex1-z

            vertex_array[i*9+3] = ff[6];  //vertex2-x
            vertex_array[i*9+4] = ff[7];  //vertex2-y
            vertex_array[i*9+5] = ff[8];  //vertex2-z

            vertex_array[i*9+6] = ff[9];  //vertex3-x
            vertex_array[i*9+7] = ff[10]; //vertex3-y
            vertex_array[i*9+8] = ff[11]; //vertex3-z

            Min_Max(ff[3],ff[6],ff[9], &min_tmp_x, &max_tmp_x);
            if(min_tmp_x < Xmin){ Xmin = min_tmp_x; }
            if(max_tmp_x > Xmax){ Xmax = max_tmp_x; }
            Min_Max(ff[4],ff[7],ff[10], &min_tmp_y, &max_tmp_y);
            if(min_tmp_y < Ymin){ Ymin = min_tmp_y; }
            if(max_tmp_y > Ymax){ Ymax = max_tmp_y; }
            Min_Max(ff[5],ff[8],ff[11], &min_tmp_z, &max_tmp_z);
            if(min_tmp_z < Zmin){ Zmin = min_tmp_z; }
            if(max_tmp_z > Zmax){ Zmax = max_tmp_z; }

            fread(end, sizeof(end),1,binaryFile);

            i++;
        }

        average_size = (fabsf(Xmax-Xmin)+fabsf(Ymax-Ymin)+fabsf(Zmax-Zmin))/3;

        for(int x = 0 ; x < i*9 ; x++){
            vertex_array[x] = (vertex_array[x]/average_size)*2;
        }

        vertex_min_max[0] = Xmin;
        vertex_min_max[1] = Xmax;
        vertex_min_max[2] = Ymin;
        vertex_min_max[3] = Ymax;
        vertex_min_max[4] = Zmin;
        vertex_min_max[5] = Zmax;

        vertex_centerxyz[0] = (Xmin+Xmax)/average_size;
        vertex_centerxyz[1] = (Ymin+Ymax)/average_size;
        vertex_centerxyz[2] = (Zmin+Zmax)/average_size;


        fclose(binaryFile);
        (*env).ReleaseStringUTFChars(filename, fileRoot);
        (*env).ReleaseFloatArrayElements(nor,normal_array,NULL);
        (*env).ReleaseFloatArrayElements(ver,vertex_array,NULL);
        (*env).ReleaseFloatArrayElements(xyz,vertex_min_max,NULL);
        (*env).ReleaseFloatArrayElements(centerxyz,vertex_centerxyz,NULL);

        return i;
    }

    void Min_Max(const float a,const float b,const float c,float *min_input,float *max_input){
        float min=0 ,max =0;

        if(a < b){
            if(a < c){
                min = a;
                if(b > c){    //a<c<b
                    max = b;
                }else{        //a<b<c
                    max = c;
                }
            }else{      //c<a<b
                min = c;
                max = b;
            }
        }else{
            if(b < c){
                min = b;
                if(a > c){     //b<c<a
                    max = a;
                }else{         //b<a<c
                    max = c;
                }
            }else{          //c<b<a
                min = c;
                max = a;
            }
        }

        *min_input = min;
        *max_input = max;
    }

    JNIEXPORT int Java_com_example_administrator_stlload_stlBinaryParser_Faces(JNIEnv *env, jobject instance,
                                                                    jstring filename_) {
        const char *filename = env->GetStringUTFChars(filename_, 0);
        int faces =0;
        FILE *binaryFile = fopen(filename,"rb");

        fseek(binaryFile,80L,SEEK_SET);
        fread(&faces, sizeof(faces),1,binaryFile);

        fclose(binaryFile);
        env->ReleaseStringUTFChars(filename_, filename);
        return faces;
    }

    JNIEXPORT int Java_com_example_administrator_stlload_stlPaser_fileSize(JNIEnv *env, jobject instance,
                                                                               jstring filename_) {
        const char *filename = env->GetStringUTFChars(filename_, 0);
        unsigned int i = 0;
        FILE *binaryFile = fopen(filename,"rb");

        fseek(binaryFile,80L,SEEK_SET);
        fread(&i, sizeof(i),1,binaryFile);

        fclose(binaryFile);
        env->ReleaseStringUTFChars(filename_, filename);


        return 84+(i*50);
    }


}