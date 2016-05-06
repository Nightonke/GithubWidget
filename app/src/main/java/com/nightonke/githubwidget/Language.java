package com.nightonke.githubwidget;

/**
 * Created by Weiping on 2016/4/30.
 */
public enum Language {

    ALL_LANGUAGE("All Language"),
    JAVASCRIPT("JavaScript"),
    CSS("CSS"),
    RUBY("Ruby"),
    PYTHON("Python"),
    CPP("C++"),
    PHP("PHP"),
    SHELL("Shell"),
    OBJECTIVE_C("Objective-C"),
    C("C"),
    GO("Go"),
    JAVA("JAVA"),
    VIML("Viml"),
    COFFEESCRIPT("CoffeeScript"),
    SCALA("Scala"),
    C_SHARP("C#"),
    CLOJURE("Clojure"),
    PERL("Perl"),
    ACTIONSCRIPT("ActionScript"),
    EMACSLISP("Emacs Lisp"),
    ERLANG("Erlang"),
    HASKELL("Haskell"),
    TYPESCRIPT("TypeScript"),
    ASSEMBLY("Assembly"),
    ELIXIR("Elixir"),
    OBJECTIVE_J("Objective-J"),
    RUST("Rust"),
    VALA("Vala"),
    JULIA("Julia"),
    VISUAL_BASIC("Visual Basic"),
    TEX("Tex"),
    R("R"),
    LUA("Lua"),
    POWERSHELL("PowerShell"),
    PROLOG("Prolog"),
    XSLT("XSLT"),
    MATLAB("Matlab"),
    OCAML("OCaml"),
    DART("Dart"),
    GROOVY("Groovy"),
    LASSO("Lasso"),
    LIVESCRIPT("LiveScript"),
    SCHEME("Scheme"),
    COMMON_LISP("Common Lisp"),
    XML("XML"),
    MIRAH("Mirah"),
    ARC("Arc"),
    DOT("DOT"),
    RACKET("Racket"),
    F_SHARP("F#"),
    D("D"),
    RAGEL_IN_RUBY_HOST("Ragel in Ruby Host"),
    HOST("Host"),
    PUPPET("Puppet"),
    UNKNOWN("Unknown");

    String v;

    Language(String v) {
        this.v = v;
    }

    public static Language fromString(String v) {
        if (v != null) {
            for (Language language : Language.values()) {
                if (v.equalsIgnoreCase(language.v)) {
                    return language;
                }
            }
        }
        return null;
    }
}
