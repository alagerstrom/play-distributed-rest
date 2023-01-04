#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <curl/curl.h>

int main(int argc, char *argv[]) {
  // Check for the correct number of arguments
  if (argc != 5) {
    fprintf(stderr, "Usage: send_message U M C R\n");
    return 1;
  }

  // Set the values of the variables
  char *U = argv[1];
  char *M = argv[2];
  char *C = argv[3];
  char *R = argv[4];

  // Create the full URL by concatenating U and /message
  char url[1024];
  snprintf(url, sizeof(url), "%s/message", U);

  // Create the payload string
  char payload[1024];
  snprintf(payload, sizeof(payload), "{\"messageType\":\"%s\", \"content\": \"%s\", \"replyTo\": \"%s\"}", M, C, R);

  CURL *curl;
  CURLcode res;

  // Initialize the curl handle
  curl = curl_easy_init();
  if (curl) {
    // Set the URL and payload
    curl_easy_setopt(curl, CURLOPT_URL, url);
    curl_easy_setopt(curl, CURLOPT_POSTFIELDS, payload);

    // Set the content type
    struct curl_slist *headers = NULL;
    headers = curl_slist_append(headers, "Content-Type: application/json");
    curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);

    // Perform the request
    res = curl_easy_perform(curl);

    // Check for errors
    if (res != CURLE_OK) {
      fprintf(stderr, "curl_easy_perform() failed: %s\n",
              curl_easy_strerror(res));
    }

    // Clean up
    curl_slist_free_all(headers);
    curl_easy_cleanup(curl);
  }

  return (int)res;
}
