
#include <vector>
#include <string>
#include <iostream>
#include <list>
#include <set>
#include <algorithm>
#include <stdint.h>

class Node {
  private:
    std::string _id;
    std::vector<Node*> _in;
    std::vector<Node*> _out;

  public:
    Node(std::string id) : _id(id) {}

    void addInput(Node* in) {
      _in.push_back(in);
      in->_out.push_back(this);
    }
    
    void addOutput(Node* out) {
      _out.push_back(out);
      out->_in.push_back(this);
    }

    void print() {
      std::cout << _id << " IN [ ";
      std::for_each(_in.begin(), _in.end(), [](Node* n) { std::cout << n->_id << " "; });
      std::cout << "]  OUT [ ";
      std::for_each(_out.begin(), _out.end(), [](Node* n) { std::cout << n->_id << " "; });
      std::cout << "] " << std::endl;
    }

    std::vector<Node*>& out() {
      return _out;
    }
    std::vector<Node*>& in() {
      return _in;
    }
    
    std::string id() {
      return _id;
    }
  
    int in_cnt() {
      return _in.size();
    }

    int out_cnt() {
      return _out.size();
    }
};

class MemoryPanes {
  private:
    int _pane_cap;
    int _cur_pane;
    int _cur_pane_size;
    std::vector<void *> _panes;

  public:
    MemoryPanes() {
      _cur_pane = 0;
      _cur_pane_size = 0;
      _pane_cap = 1024;
      _panes.push_back(malloc(_pane_cap));
    }

    ~MemoryPanes() {
      std::for_each(_panes.begin(), _panes.end(), [](void *mem) { free(mem); }); 
    }

    int curr_pane_remaing() {
      return _pane_cap - _cur_pane_size + 1;
    }

    void alloacte_new_pane() {
      _panes.push_back(malloc(_pane_cap));
      _cur_pane++;
      _cur_pane_size = 0;
    }

    void* allocate(size_t s) {
      if (s > curr_pane_remaing()) {
        alloacte_new_pane();
      }
      void* mem = static_cast<void*>(reinterpret_cast<char*>(_panes[_cur_pane]) + _cur_pane_size);
      _cur_pane_size += s;
      return mem;
    }
};

MemoryPanes mem;

class Graph {
  private:
    Node* _root;
    std::vector<Node*> _nodes;
    
  public:
    using node_processor = void (*) (Node* );

    Graph() {
      _root = new Node("ROOT");
      _nodes.push_back(_root);
    }

    Node* root() {
      return _root;
    }

    void addNode(Node* node) {
      _nodes.push_back(node);
    }

    struct INode {
      Node* _n; 
      uint32_t _idx;

      INode(Node* n, uint32_t idx) {
        _n = n;
        _idx = idx;
      }

      uint32_t idx() {
        return _idx;
      }

      void inc_idx() {
        _idx++;
      }

      Node* node() {
        return _n;
      }

      void* operator new(size_t n) {
        return mem.allocate(n);
      }
    };

    void walk_dfs(node_processor func) {
      std::cout << "\nDFS WALK " << std::endl;
      std::set<Node*> visited;
      std::vector<INode*> worklist;

      worklist.push_back(new INode(_root, 0));
      while (!worklist.empty()) {
        INode* n = worklist.back();
        
        if (visited.find(n->node()) == visited.end()) {
          visited.insert(n->node());
        }

        if (n->idx() == n->node()->out_cnt()) {
          func(n->node());
          worklist.pop_back();
        } else {
          if (visited.find(n->node()->out()[n->idx()]) == visited.end()) {
	    worklist.push_back(new INode(n->node()->out()[n->idx()], 0));
          } 
          n->inc_idx();
        }
      }
    }

    void print_bfs() {
      std::cout << "\nBFS TRAVERSAL " << std::endl;
      std::list<Node*> worklist;
      std::set<Node*> visited;
      worklist.push_back(_root);
      while (!worklist.empty()) {
        Node* n = worklist.front();
        worklist.pop_front();
        n->print();
        for (auto itr = n->out().begin(); itr != n->out().end(); itr++) {
           if (visited.find(*itr) == visited.end()) {
              visited.insert(*itr);
              worklist.push_back(*itr);
           }
        }
      }
    }
};

void testGraph1() {
  std::cout << "\n\ttestGraph1 : " << std::endl;
  /*          
               _____________              
               |            |
             ROOT           |
        ______|_______      |
       /   |   |   |  \     |
      P1  P2  P3  P4  P5    |
      |    |   |   |   |    |
      \    /   |   |   |    |
       ADD     /   |   |    |
        |     /   /   /     |
        \    /   /   /      |
          MUL   /   /       |
           |   /   /        |
            SUB   /         | 
             |   /          |
              DIV           |
               |            |
              RETURN        |
               |____________|

  */       
  Graph* g = new Graph();
  Node* param1 = new Node("PARAM1");
  Node* param2 = new Node("PARAM2");
  Node* param3 = new Node("PARAM3");
  Node* param4 = new Node("PARAM4");
  Node* param5 = new Node("PARAM5");

  g->root()->addOutput(param1);
  g->root()->addOutput(param2);
  g->root()->addOutput(param3);
  g->root()->addOutput(param4);
  g->root()->addOutput(param5);

  Node* add = new Node("ADD");
  add->addInput(param1);
  add->addInput(param2);
 
  Node* mul = new Node("MUL");
  mul->addInput(add);
  mul->addInput(param3);

  Node* sub = new Node("SUB");
  sub->addInput(mul);
  sub->addInput(param4);
  
  Node* div = new Node("DIV");
  div->addInput(sub);
  div->addInput(param5);

  Node* ret = new Node("RETURN");
  ret->addInput(div);
  ret->addOutput(g->root());

  g->walk_dfs([](Node* n) { n->print(); });
  g->print_bfs();
}

/*

        testGraph1 :

DFS WALK
RETURN IN [ DIV ]  OUT [ ROOT ]
DIV IN [ SUB PARAM5 ]  OUT [ RETURN ]
SUB IN [ MUL PARAM4 ]  OUT [ DIV ]
MUL IN [ ADD PARAM3 ]  OUT [ SUB ]
ADD IN [ PARAM1 PARAM2 ]  OUT [ MUL ]
PARAM1 IN [ ROOT ]  OUT [ ADD ]
PARAM2 IN [ ROOT ]  OUT [ ADD ]
PARAM3 IN [ ROOT ]  OUT [ MUL ]
PARAM4 IN [ ROOT ]  OUT [ SUB ]
PARAM5 IN [ ROOT ]  OUT [ DIV ]
ROOT IN [ RETURN ]  OUT [ PARAM1 PARAM2 PARAM3 PARAM4 PARAM5 ]

BFS TRAVERSAL
ROOT IN [ RETURN ]  OUT [ PARAM1 PARAM2 PARAM3 PARAM4 PARAM5 ]
PARAM1 IN [ ROOT ]  OUT [ ADD ]
PARAM2 IN [ ROOT ]  OUT [ ADD ]
PARAM3 IN [ ROOT ]  OUT [ MUL ]
PARAM4 IN [ ROOT ]  OUT [ SUB ]
PARAM5 IN [ ROOT ]  OUT [ DIV ]
ADD IN [ PARAM1 PARAM2 ]  OUT [ MUL ]
MUL IN [ ADD PARAM3 ]  OUT [ SUB ]
SUB IN [ MUL PARAM4 ]  OUT [ DIV ]
DIV IN [ SUB PARAM5 ]  OUT [ RETURN ]
RETURN IN [ DIV ]  OUT [ ROOT ]
ROOT IN [ RETURN ]  OUT [ PARAM1 PARAM2 PARAM3 PARAM4 PARAM5 ]
*/
void testGraph2() {
  std::cout << "\n\ttestGraph2 : " << std::endl;
  /*          
         _____________              
         |           |
        ROOT         |
   ______|_____      |
   /   |   |   \     |
  P1  P2  P3  P4     |
  |   |    |   |     |
  |   \    /   |     |
  |    ADD     |     |
  |     |      /     |
  |     |     /      |
  |    / \   /       |
   \  /   \ /        |
    SUB   MUL        | 
      \   /          |
       \ /           |
       DIV           |
        |            |
      RETURN---------|
  */       
  Graph* g = new Graph();
  Node* param1 = new Node("PARAM1");
  Node* param2 = new Node("PARAM2");
  Node* param3 = new Node("PARAM3");
  Node* param4 = new Node("PARAM4");

  g->root()->addOutput(param1);
  g->root()->addOutput(param2);
  g->root()->addOutput(param3);
  g->root()->addOutput(param4);

  Node* add = new Node("ADD");
  add->addInput(param2);
  add->addInput(param3);
 
  Node* sub = new Node("SUB");
  sub->addInput(param1);
  sub->addInput(add);

  Node* mul = new Node("MUL");
  mul->addInput(add);
  mul->addInput(param4);
  
  Node* div = new Node("DIV");
  div->addInput(sub);
  div->addInput(mul);

  Node* ret = new Node("RETURN");
  ret->addInput(div);
  ret->addOutput(g->root());

  g->walk_dfs([](Node* n) { n->print(); });
  g->print_bfs();
}
/*
        testGraph2 :

DFS WALK
RETURN IN [ DIV ]  OUT [ ROOT ]
DIV IN [ SUB MUL ]  OUT [ RETURN ]
SUB IN [ PARAM1 ADD ]  OUT [ DIV ]
PARAM1 IN [ ROOT ]  OUT [ SUB ]
MUL IN [ ADD PARAM4 ]  OUT [ DIV ]
ADD IN [ PARAM2 PARAM3 ]  OUT [ SUB MUL ]
PARAM2 IN [ ROOT ]  OUT [ ADD ]
PARAM3 IN [ ROOT ]  OUT [ ADD ]
PARAM4 IN [ ROOT ]  OUT [ MUL ]
ROOT IN [ RETURN ]  OUT [ PARAM1 PARAM2 PARAM3 PARAM4 ]

BFS TRAVERSAL
ROOT IN [ RETURN ]  OUT [ PARAM1 PARAM2 PARAM3 PARAM4 ]
PARAM1 IN [ ROOT ]  OUT [ SUB ]
PARAM2 IN [ ROOT ]  OUT [ ADD ]
PARAM3 IN [ ROOT ]  OUT [ ADD ]
PARAM4 IN [ ROOT ]  OUT [ MUL ]
SUB IN [ PARAM1 ADD ]  OUT [ DIV ]
ADD IN [ PARAM2 PARAM3 ]  OUT [ SUB MUL ]
MUL IN [ ADD PARAM4 ]  OUT [ DIV ]
DIV IN [ SUB MUL ]  OUT [ RETURN ]
RETURN IN [ DIV ]  OUT [ ROOT ]
ROOT IN [ RETURN ]  OUT [ PARAM1 PARAM2 PARAM3 PARAM4 ]
*/

int main() {
  testGraph1();
  testGraph2();
  return 0;
}

