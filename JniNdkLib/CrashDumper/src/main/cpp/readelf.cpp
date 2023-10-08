#include "readelf.h"

#include <elf.h>
#include <string>
#include <android/log.h>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,"yc_testjni_readelf" ,__VA_ARGS__)

#define SEC_NAME_BUILD_ID ".note.gnu.build-id"

#if defined(__aarch64__)
#define Elf_Ehdr Elf64_Ehdr
#define Elf_Shdr Elf64_Shdr
#elif defined(__arm__)
#define Elf_Ehdr Elf32_Ehdr
#define Elf_Shdr Elf32_Shdr
#endif

std::string getBuildIdFromFile(const char *path) {
    FILE *elfFile = fopen(path, "rb");
    if (elfFile) {
        fseek(elfFile, 0L, SEEK_END);
        long len = ftell(elfFile);
        fseek(elfFile, 0L, SEEK_SET);
        auto *elfData = (unsigned char *) malloc(len);
        fread(elfData, sizeof(char), len, elfFile);
        fclose(elfFile);
        std::string buildIdStr = getBuildId(elfData);
        free(elfData);
        return buildIdStr;
    } else {
        return "";
    }
}


std::string getBuildId(unsigned char *elfData) {
    Elf_Ehdr *elfHeader = (Elf_Ehdr *) elfData;
    Elf_Shdr *namesSectionHeader = (Elf_Shdr *) (elfData + elfHeader->e_shoff +
                                                 elfHeader->e_shstrndx * elfHeader->e_shentsize);

    char *sectionNames = (char *) (elfData + namesSectionHeader->sh_offset);
    for (int i = 0; i < elfHeader->e_shnum; ++i) {
        Elf_Shdr *sectionHeader = (Elf_Shdr *) (elfData + elfHeader->e_shoff +
                                                i * elfHeader->e_shentsize);
        if (strcmp((const char *) sectionNames + sectionHeader->sh_name, SEC_NAME_BUILD_ID) == 0) {
            char *buildId = (char *) malloc(sectionHeader->sh_size);
            memcpy(buildId, elfData + sectionHeader->sh_offset, sectionHeader->sh_size);
            std::string buildIdStr;
            // offset 0x10 (16)
            // +----------------+
            // |     namesz     |   32-bit, size of "name" field
            // +----------------+
            // |     descsz     |   32-bit, size of "desc" field
            // +----------------+
            // |      type      |   32-bit, vendor specific "type"
            // +----------------+
            // |      name      |   "namesz" bytes, null-terminated string
            // +----------------+
            // |      desc      |   "descsz" bytes, binary data
            // +----------------+
            // https://interrupt.memfault.com/blog/gnu-build-id-for-firmware
            //
            // 04000000 14000000 03000000 47 4e 55 00
            // namesz   descsz   type     G  N  U  0
            //    4   +    4   +   4    +      4  = 16
            int nameSize = *buildId;
            int descOffset = 4 + 4 + 4 + nameSize;
            for (int j = descOffset; j < sectionHeader->sh_size; ++j) {
                char ch[3] = {0};
                sprintf(ch, "%02x", buildId[j]);
                buildIdStr += ch;
            }
            free(buildId);
            return buildIdStr;
        }
    }
    return "";
}