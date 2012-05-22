LOCAL_PATH := .
include ../Makefile.vars
include ./Common.mk
PREBUILT_DIR = ../jni-prebuilt/$(os)-$(arch)/
LIBNAME := osmand.lib
LIBOSNAME := osmand-$(os)-$(arch).lib

RUNFILE := osmand_main

LOCAL_SRC_FILES += src/osmand_main.cpp

#LDFLAGS = -Wl,--hash-style=both -fPIC
LDFLAGS = -Wall -fPIC -Wl,-Bsymbolic
LDRUNFLAGS = -Wl,--hash-style=both
#  --trace -Bsymbolic -Bsymbolic-functions

LDLIBS = -L$(PREBUILT_DIR) -lskia -lproto -lpthread -lrt -lft2 -lexpat -lz -lpng 
CPP_FILE_EXTENSION = cpp
OBJECTS = $(LOCAL_SRC_FILES:src/%.$(CPP_FILE_EXTENSION)=build/obj-$(os)-$(arch)/%.o)

C_INCLUDES := $(addprefix -I, $(LOCAL_C_INCLUDES))

CPPFLAGS := \
	-DGOOGLE_PROTOBUF_NO_RTTI \
	-DSK_ALLOW_STATIC_GLOBAL_INITIALIZERS=0 \
	-DSK_RELEASE \
	-DGR_RELEASE=1 \
	-c -fPIC -I$(JAVA_HOME)/include \
	-DHASH_MAP_GNU \
	$(C_INCLUDES)
	
#target : ../$(RUNFILE)	
target : $(PREBUILT_DIR)/$(LIBNAME)
#target : $(PREBUILT_DIR)/$(LIBNAME) ../$(RUNFILE)


../$(RUNFILE) : $(OBJECTS) $(PREBUILT_DIR)/libskia.a $(PREBUILT_DIR)/libproto.a Makefile
	$(CXX) $(LDRUNFLAGS) -o ../$(RUNFILE)  $(OBJECTS) $(LDLIBS)
	@chmod +x ../$(RUNFILE)

$(PREBUILT_DIR)/$(LIBNAME) : build/$(LIBOSNAME)
	cp build/$(LIBOSNAME) $(PREBUILT_DIR)/$(LIBNAME)


build/$(LIBOSNAME): $(OBJECTS) $(PREBUILT_DIR)/libskia.a $(PREBUILT_DIR)/libproto.a Makefile
	$(CXX) -shared -o build/$(LIBOSNAME) $(OBJECTS) $(LDFLAGS) $(LDLIBS)
	
build/obj-$(os)-$(arch)/%.o : src/%.$(CPP_FILE_EXTENSION) $(LOCAL_C_INCLUDES)
	@mkdir -p `dirname $@`
	$(CXX) -o $@ -c src/$*.$(CPP_FILE_EXTENSION) $(CPPFLAGS)
	
clean:
	$(RM) $(OBJECTS) build/$(LIBNAME)