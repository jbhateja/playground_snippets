
#include <vector>
#include <string>
#include <iostream>
#include <list>
#include <set>
#include <algorithm>

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
};

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

    void print_bfs() {
      std::cout << "BFS TRAVERSAL " << std::endl;
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


int main() {
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

  g->print_bfs();

  return 0;
}
