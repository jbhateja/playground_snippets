clang -mavx512f -S -O0 loop_deps.c -Xclang -disable-O0-optnone -emit-llvm
opt -p mem2reg,simplifycfg loop_deps.ll -S -o loop_deps_opt.ll -debug-pass-manager
opt -passes=loop-rotate loop_deps_opt.ll -o loop_deps_opt1.ll -S --debug-pass-manager
#opt -passes=loop-vectorize loop_deps_opt1.ll -o loop_deps_opt2.ll -S --debug-pass-manager -debug
#opt --print-passes
opt -passes=slp-vectorizer loop_deps_opt1.ll -o loop_deps_opt2.ll -S --debug-pass-manager -debug
llc loop_deps_opt2.ll
