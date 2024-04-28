

#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdatomic.h>
#include <assert.h>

#define DEBUGT(...) \
  do  { \
    assert(td != NULL); \
    fprintf(stdout, "[tid %d] ", td->tid); \
    fprintf(stdout, __VA_ARGS__); \
    fflush(stdout); \
  } while (0)

#define DEBUG(...) \
  do  { \
    fprintf(stdout, __VA_ARGS__); \
    fflush(stdout); \
  } while (0)

typedef struct _node {
   int value;
   int tid;
   struct _node* next;
} node;

typedef struct _list {
   struct _node* head;
} list;

typedef struct _thread_data {
  int tid;
  int start;
  int end;
  list* link;
} thread_data;

void* kernel(void* data) {
  thread_data* td = (thread_data*)data;
  DEBUGT("Entering thread.\n");
  for (int i = td->start; i < td->end; i++) {
      node* n = (node*)malloc(sizeof(node));
      n->value = i;
      n->tid = td->tid;
      int contended = 0;
      do {
         if (contended) {
           DEBUGT("Re-attermping to add new node [%d][%d][%p] to list head.\n", n->tid, n->value, n);
         } else {
           DEBUGT("Adding new node [%d][%d][%p] to list head.\n", n->tid, n->value, n);
         }
         n->next = td->link->head;
         /*
           - lock cmpexcg updating memory
             - RMW
             - RFO
             - Eviction out of store buffer needs memory barrier
         */
         __asm__ volatile (
             "lock cmpxchg %1, %0  \n\t"
           : "+m"(td->link->head), "+r"(n)
           : "a"(n->next)
           : "cc", "memory"
         ); 
         contended++;
      } while (n->next == n);
  }  
  DEBUGT("Exiting thread.\n"); 
  return td;
}

void print_node(node* n) {
   DEBUG("node[tid, value, next] = %p[%d, %d, %p]\n", n, n->tid, n->value, n->next);
}

int main (int argc, char* argv[]) {
  if (argc != 3) {
     return fprintf(stderr, "Incorrect Arguments!\n application <thead-count> <node-count>\n");
  }
  int tcount = atoi(argv[1]);
  int ncount = atoi(argv[2]);
  pthread_t tids[tcount];
  list* link = (list*)malloc(sizeof(list));
  link->head = NULL;
  thread_data* tds = (thread_data*)malloc(sizeof(thread_data) * tcount); 
  for (int i = 0; i < tcount; i++) {
    tds[i].tid = i + 1;
    tds[i].start = i * ncount;
    tds[i].end = i * ncount + ncount;
    tds[i].link = link; 
  }
  DEBUG("Creating %d worker thread[s] for generating %d nodes.\n", tcount, ncount);
  for (int i = 0; i < tcount; i++) {
    pthread_create(&tids[i], NULL, kernel, &tds[i]);
  } 
  for (int i = 0; i < tcount; i++) {
    pthread_join(tids[i], NULL);
  }
  DEBUG("Print unified list.\n");
  node* itr = link->head;
  int ictr = 0;
  while(itr) {
     print_node(itr);
     itr = itr->next;
     ictr++;
  }
  assert(ictr == ncount * tcount);
  DEBUG("Total nodes in unified list = %d\n", ictr);
  return 0;
}

