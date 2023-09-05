
#include <string>

/**
 * return the buildId of shared object
 *
 * @param elfData so file data
 * @return buildId
 */
std::string getBuildId(unsigned char *elfData);
/**
 * return the buildId of shared object
 *
 * @param elfData so file path
 * @return buildId
 */
std::string getBuildIdFromFile(const char *path);
