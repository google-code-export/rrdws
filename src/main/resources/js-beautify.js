/*
 
JS Beautifier
---------------
$Date$
$Revision$
 
 
Written by Einars Lielmanis, <einars@gmail.com>
http://elfz.laacz.lv/beautify/
 
Originally converted to javascript by Vital, <vital76@gmail.com>
http://my.opera.com/Vital/blog/2007/11/21/javascript-beautify-on-
javascript-translated
 
 
You are free to use this in any way you want, in case you find this
useful or working for you.
 
Usage:
js_beautify(js_source_text);
js_beautify(js_source_text, options);
 
The options are:
   * indent_size (default 4) — indentation size,
   * indent_char (default space) — character to indent with,
   * preserve_newlines (default true) — whether existing line breaks
     should be preserved,
   * indent_level (default 0) — initial indentation level, you probably
     won't need this ever,
   * indent_comm (default "indent") - if passed the value "unindent"
     comments will be unindented
 
e.g
 
js_beautify(js_source_text, {indent_size: 1, indent_char: '\t'});
 
 
*/
"use strict";
var js_beautify = function (js_source_text, options) {
    if (new RegExp(/>\s*<\/[a-z]+>/i).test(js_source_text) === true && new RegExp(/^(\s*<)/).test(js_source_text) === true && new RegExp(/(>\s*)$/).test(js_source_text) === true) {
        js_summary = function () {
            return;
        };
        return "Error: Input appears to be markup, but the application is set to beautify JavaScript.";
    } else if (new RegExp(/[a-z]+[a-z0-9]*\s*\{\s*[a-z]+(\-[a-z]+)*\s*\:\s*[a-z]+(\-[a-z]+)*\s*;/i).test(js_source_text) === true && new RegExp(/if\s*\(\s*[a-z0-9]+\s*((={2,3})|(\!={1,2}))\s*[a-z0-9]+\s*\)\s*\{/i).test(js_source_text) === false) {
        js_summary = function () {
            return;
        };
        return "Error: Input appears to be CSS, but the application is set to beautify JavaScript.";
    }
    options = options || {};
    var i, input, output, rvalue, token_text, last_type, last_text, last_word, current_mode, modes, indent_string, whitespace, wordchar, punct, parser_pos, end_delim, line_starters, in_case, prefix, token_type, do_block_just_closed, t, var_line, var_line_tainted, if_line_flag, indent_level, start_delim, opt_preserve_newlines = typeof options.preserve_newlines === 'undefined' ? true: options.preserve_newlines,
    size = js_source_text.length,
    opt_indent_size = (options.indent_size) ? options.indent_size: 4,
    opt_indent_char = (options.indent_char) ? options.indent_char: ' ',
    opt_indent_level = (options.indent_level) ? options.indent_level: 0,
    opt_indent_comm = (options.indent_comm) ? options.indent_comm: "indent",

    //This function corrects a beautification flaw between parenthesis
    //contained anonymous functions that directly follow a terminating
    //syntax character.  This function moves the anonymous function onto
    //a new line only if the anonymous function is not a string literal.
    //This function also applies the correct indention immediately after
    //the new line.
    fixSelfAnon = function (x) {
        if (rvalue.indexOf(x + " (function") === -1) {
            return;
        }
        var a = [],
        b,
        c = "",
        d = [],
        e,
        f,
        g = new RegExp(/\s/),
        h = [];
        for (b = 0; b < rvalue.length; b += 1) {
            if (rvalue.charAt(b) === "\"" || rvalue.charAt(b) === "'") {
                if (c === "") {
                    c = b;
                } else if (rvalue.charAt(c) === "\"" && rvalue.charAt(b) === "\"") {
                    d.push(c + "|" + b);
                    c = "";
                } else if (rvalue.charAt(c) === "\'" && rvalue.charAt(b) === "\'") {
                    d.push(c + "|" + b);
                    c = "";
                }
            }
            if (rvalue.charAt(b) === x && rvalue.charAt(b + 1) === " " && rvalue.charAt(b + 2) === "(" && rvalue.charAt(b + 3) === "f" && rvalue.charAt(b + 4) === "u" && rvalue.charAt(b + 5) === "n" && rvalue.charAt(b + 6) === "c" && rvalue.charAt(b + 7) === "t" && rvalue.charAt(b + 8) === "i" && rvalue.charAt(b + 9) === "o" && rvalue.charAt(b + 10) === "n") {
                a.push(b);
                f = "";
                for (e = b; e > -1; e -= 1) {
                    if (rvalue.charAt(e) === "\n") {
                        break;
                    }

                    if (g.test(rvalue.charAt(e)) === true && g.test(rvalue.charAt(e - 1)) === true && rvalue.charAt(e) !== "\n") {
                        f += rvalue.charAt(e);
                    }
                }
                h.push(f);
            }
        }
        for (e = 0; e < d.length; e += 1) {
            for (c = 0; c < a.length; c += 1) {
                if (a[c] > d[e].split("|")[0] && a[c] < d[e].split("|")[1]) {
                    a[c] = -1;
                }
            }
        }
        rvalue = rvalue.split("");
        for (c = 0; c < a.length; c += 1) {
            if (a[c] !== -1) {
                rvalue[a[c] + 1] = "\n" + h[c];
            }
        }
        rvalue = rvalue.join("");
    },
    print_newline = function (ignore_repeated) {
        ignore_repeated = typeof ignore_repeated === 'undefined' ? true: ignore_repeated;
        if_line_flag = false;
        trim_output();
        if (!output.length) {
            return;
        }
        if (output[output.length - 1] !== "\n" || !ignore_repeated) {
            output.push("\n");
        }
        for (var i = 0; i < indent_level; i += 1) {
            output.push(indent_string);
        }
    },
    print_space = function () {
        var last_output = output.length ? output[output.length - 1] : ' ';
        if (last_output !== ' ' && last_output !== '\n' && last_output !== indent_string && last_output !== "(" && last_output !== ".") {
            output.push(' ');
        }
    },
    print_token = function () {
        output.push(token_text);
    },
    indent = function () {
        indent_level += 1;
    },
    unindent = function () {
        if (indent_level) {
            indent_level -= 1;
        }
    },
    remove_indent = function () {
        if (output.length && output[output.length - 1] === indent_string) {
            output.pop();
        }
    },
    set_mode = function (mode) {
        modes.push(current_mode);
        current_mode = mode;
    },
    restore_mode = function () {
        do_block_just_closed = current_mode === 'DO_BLOCK';
        current_mode = modes.pop();
    },
    in_array = function (what, arr) {
        for (var i = 0; i < arr.length; i += 1) {
            if (arr[i] === what) {
                return true;
            }
        }
        return false;
    },
    get_next_token = function () {
        var t, resulting_string, sep, esc, comment, n_newlines = 0,
        c = '',
        wanted_newline = false;
        do {
            if (parser_pos >= input.length) {
                return ['', 'TK_EOF'];
            }
            c = input.charAt(parser_pos);
            parser_pos += 1;
            if (c === "\n") {
                n_newlines += 1;
            }
        } while (in_array(c, whitespace));
        if (opt_preserve_newlines) {
            if (n_newlines > 1) {
                for (i = 0; i < 2; i += 1) {
                    print_newline(i === 0);
                }
            }
            wanted_newline = (n_newlines === 1);
        }
        if (in_array(c, wordchar)) {
            if (parser_pos < input.length) {
                while (in_array(input.charAt(parser_pos), wordchar)) {
                    c += input.charAt(parser_pos);
                    parser_pos += 1;
                    if (parser_pos === input.length) {
                        break;
                    }
                }
            }
            if (parser_pos !== input.length && c.match(/^[0-9]+[Ee]$/) && input.charAt(parser_pos) === '-') {
                parser_pos += 1;
                t = get_next_token(parser_pos);
                c += '-' + t[0];
                return [c, 'TK_WORD'];
            }
            if (c === 'in') {
                return [c, 'TK_OPERATOR'];
            }
            if (wanted_newline && last_type !== 'TK_OPERATOR' && !if_line_flag) {
                print_newline();
            }
            return [c, 'TK_WORD'];
        }
        if (c === '(' || c === '[') {
            return [c, 'TK_START_EXPR'];
        }
        if (c === ')' || c === ']') {
            return [c, 'TK_END_EXPR'];
        }
        if (c === '{') {
            return [c, 'TK_START_BLOCK'];
        }
        if (c === '}') {
            return [c, 'TK_END_BLOCK'];
        }
        if (c === ';') {
            return [c, 'TK_SEMICOLON'];
        }
        if (c === '/') {
            comment = '';
            if (input.charAt(parser_pos) === '*') {
                parser_pos += 1;
                if (parser_pos < input.length) {
                    while (! (input.charAt(parser_pos) === '*' && input.charAt(parser_pos + 1) && input.charAt(parser_pos + 1) === '/') && parser_pos < input.length) {
                        comment += input.charAt(parser_pos);
                        parser_pos += 1;
                        if (parser_pos >= input.length) {
                            break;
                        }
                    }
                }
                parser_pos += 2;
                return ['/*' + comment + '*/', 'TK_BLOCK_COMMENT'];
            }
            if (input.charAt(parser_pos) === '/') {
                comment = c;
                while (input.charAt(parser_pos) !== "\x0d" && input.charAt(parser_pos) !== "\x0a") {
                    comment += input.charAt(parser_pos);
                    parser_pos += 1;
                    if (parser_pos >= input.length) {
                        break;
                    }
                }
                parser_pos += 1;
                if (wanted_newline) {
                    print_newline();
                }
                return [comment, 'TK_COMMENT'];
            }
        }
        if (c === "'" || c === '"' || (c === '/' && ((last_type === 'TK_WORD' && last_text === 'return') || (last_type === 'TK_START_EXPR' || last_type === 'TK_END_BLOCK' || last_type === 'TK_OPERATOR' || last_type === 'TK_EOF' || last_type === 'TK_SEMICOLON')))) {
            sep = c;
            esc = false;
            resulting_string = '';
            if (parser_pos < input.length) {
                while (esc || input.charAt(parser_pos) !== sep) {
                    resulting_string += input.charAt(parser_pos);
                    if (!esc) {
                        esc = input.charAt(parser_pos) === '\\';
                    } else {
                        esc = false;
                    }
                    parser_pos += 1;
                    if (parser_pos >= input.length) {
                        break;
                    }
                }
            }
            parser_pos += 1;
            resulting_string = sep + resulting_string + sep;
            if (sep === '/') {
                while (parser_pos < input.length && in_array(input.charAt(parser_pos), wordchar)) {
                    resulting_string += input.charAt(parser_pos);
                    parser_pos += 1;
                }
            }
            return [resulting_string, 'TK_STRING'];
        }
        if (in_array(c, punct)) {
            while (parser_pos < input.length && in_array(c + input.charAt(parser_pos), punct)) {
                c += input.charAt(parser_pos);
                parser_pos += 1;
                if (parser_pos >= input.length) {
                    break;
                }
            }
            return [c, 'TK_OPERATOR'];
        }
        return [c, 'TK_UNKNOWN'];
    };
    indent_string = '';
    do {
        opt_indent_size -= 1;
        indent_string += opt_indent_char;
    } while (opt_indent_size > 0);
    indent_level = opt_indent_level;
    input = js_source_text;
    last_word = '';
    last_type = 'TK_START_EXPR';
    last_text = '';
    output = [];
    do_block_just_closed = false;
    var_line = false;
    var_line_tainted = false;
    whitespace = "\n\r\t ".split('');
    wordchar = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_$'.split('');
    punct = '+ - * / % & ++ -- = += -= *= /= %= == === != !== > < >= <= >> << >>> >>>= >>= <<= && &= | || ! !! , : ? ^ ^= |= ::'.split(' ');
    line_starters = 'continue,try,throw,return,var,if,switch,case,default,for,while,break,function'.split(',');
    current_mode = 'BLOCK';
    modes = [current_mode];
    parser_pos = 0;
    in_case = false;
    while (true) {
        t = get_next_token(parser_pos);
        token_text = t[0];
        token_type = t[1];
        if (token_type === 'TK_EOF') {
            break;
        }
        switch (token_type) {
        case 'TK_START_EXPR':
            var_line = false;
            set_mode('EXPRESSION');
            if (last_type !== 'TK_WORD' && last_type !== 'TK_OPERATOR' && last_type !== 'TK_START_EXPR' && last_type !== 'TK_END_EXPR') {
                print_space();
            } else if (in_array(last_word, line_starters)) {
                print_space();
            } else if (last_word === 'catch') {
                print_space();
            }
            print_token();
            break;
        case 'TK_END_EXPR':
            print_token();
            restore_mode();
            break;
        case 'TK_START_BLOCK':
            if (last_word === 'do') {
                set_mode('DO_BLOCK');
            } else {
                set_mode('BLOCK');
            }
            if (last_type !== 'TK_OPERATOR' && last_type !== 'TK_START_EXPR') {
                if (last_type === 'TK_START_BLOCK') {
                    print_newline();
                } else {
                    print_space();
                }
            }
            print_token();
            indent();
            break;
        case 'TK_END_BLOCK':
            if (last_type === 'TK_START_BLOCK') {
                trim_output();
                unindent();
            } else {
                unindent();
                print_newline();
            }
            print_token();
            restore_mode();
            break;
        case 'TK_WORD':
            if (do_block_just_closed) {
                if (last_text !== "[") {
                    print_space();
                }
                if (in_array(token_text, line_starters)) {
                    token_text = token_text + " ";
                }
                print_token();
                break;
            }
            if (token_text === 'case' || token_text === 'default') {
                if (last_text === ':') {
                    remove_indent();
                } else {
                    unindent();
                    print_newline();
                    indent();
                }
                print_token();
                in_case = true;
                break;
            }
            prefix = 'NONE';
            if (last_type === 'TK_END_BLOCK') {
                if (!in_array(token_text.toLowerCase(), ['else', 'catch', 'finally'])) {
                    prefix = 'NEWLINE';
                } else {
                    prefix = 'SPACE';
                    print_space();
                }
            } else if (last_type === 'TK_SEMICOLON' && (current_mode === 'BLOCK' || current_mode === 'DO_BLOCK')) {
                prefix = 'NEWLINE';
            } else if (last_type === 'TK_SEMICOLON' && current_mode === 'EXPRESSION') {
                prefix = 'SPACE';
            } else if (last_type === 'TK_STRING') {
                prefix = 'NEWLINE';
            } else if (last_type === 'TK_WORD') {
                prefix = 'SPACE';
            } else if (last_type === 'TK_START_BLOCK') {
                prefix = 'NEWLINE';
            } else if (last_type === 'TK_END_EXPR') {
                print_space();
                prefix = 'NEWLINE';
            }
            if (last_type !== 'TK_END_BLOCK' && in_array(token_text.toLowerCase(), ['else', 'catch', 'finally'])) {
                print_newline();
            } else if (in_array(token_text, line_starters) || prefix === 'NEWLINE') {
                if (last_text === 'else') {
                    print_space();
                } else if ((last_type === 'TK_START_EXPR' || last_text === '=') && token_text === 'function') {} else if (last_type === 'TK_WORD' && (last_text === 'return' || last_text === 'throw')) {
                    print_space();
                } else if (last_type !== 'TK_END_EXPR') {
                    if ((last_type !== 'TK_START_EXPR' || token_text !== 'var') && last_text !== ':') {
                        if (token_text === 'if' && last_type === 'TK_WORD' && last_word === 'else') {
                            print_space();
                        } else {
                            print_newline();
                        }
                    }
                } else {
                    if (in_array(token_text, line_starters) && last_text !== ')') {
                        print_newline();
                    }
                }
            } else if (prefix === 'SPACE') {
                print_space();
            }
            print_token();
            last_word = token_text;
            if (token_text === 'var') {
                var_line = true;
                var_line_tainted = false;
            }
            if (token_text === 'if' || token_text === 'else') {
                if_line_flag = true;
            }
            break;
        case 'TK_SEMICOLON':
            print_token();
            var_line = false;
            break;
        case 'TK_STRING':
            if (last_type === 'TK_START_BLOCK' || last_type === 'TK_END_BLOCK' || last_type === 'TK_SEMICOLON') {
                print_newline();
            } else if (last_type === 'TK_WORD') {
                print_space();
            }
            print_token();
            break;
        case 'TK_OPERATOR':
            start_delim = true;
            end_delim = true;
            if (var_line && token_text !== ',') {
                var_line_tainted = true;
                if (token_text === ':') {
                    var_line = false;
                }
            }
            if (token_text === ':' && in_case) {
                print_token();
                print_newline();
                break;
            }
            if (token_text === '::') {
                print_token();
                break;
            }
            in_case = false;
            if (token_text === ',') {
                if (var_line) {
                    if (var_line_tainted) {
                        print_token();
                        print_newline();
                        var_line_tainted = false;
                    } else {
                        print_token();
                        print_space();
                    }
                } else if (last_type === 'TK_END_BLOCK') {
                    print_token();
                    print_newline();
                } else {
                    if (current_mode === 'BLOCK') {
                        print_token();
                        print_newline();
                    } else {
                        print_token();
                        print_space();
                    }
                }
                break;
            } else if (token_text === '--' || token_text === '++') {
                if (last_text === ';') {
                    start_delim = true;
                    end_delim = false;
                } else {
                    start_delim = false;
                    end_delim = false;
                }
            } else if (token_text === '!' && last_type === 'TK_START_EXPR') {
                start_delim = false;
                end_delim = false;
            } else if (last_type === 'TK_OPERATOR') {
                start_delim = false;
                end_delim = false;
            } else if (last_type === 'TK_END_EXPR') {
                start_delim = true;
                end_delim = true;
            } else if (token_text === '.') {
                start_delim = false;
                end_delim = false;
            } else if (token_text === ':') {
                if (last_text.match(/^\d+$/)) {
                    start_delim = true;
                } else {
                    start_delim = false;
                }
            }
            if (start_delim) {
                print_space();
            }
            print_token();
            if (end_delim) {
                print_space();
            }
            break;
        case 'TK_BLOCK_COMMENT':
            print_newline();
            if (opt_indent_comm === "noindent") {
                for (i = indent_level + 1; i > 0; i -= 1) {
                    remove_indent();
                }
            }
            print_token();
            print_newline();
            break;
        case 'TK_COMMENT':
            if (opt_indent_comm === "noindent") {
                for (i = indent_level + 1; i > 0; i -= 1) {
                    remove_indent();
                }

            } else {
                print_space();
            }
            print_token();
            print_newline();
            break;
        case 'TK_UNKNOWN':
            print_token();
            break;
        }
        last_type = token_type;
        last_text = token_text;
    }
    rvalue = output.join('');
    fixSelfAnon("}");
    fixSelfAnon(";");

    //This line was added to prevent the addition of a space in
    //what should be an immediate function invocation
    rvalue = rvalue.replace(/\} \(\)/g, '}()').replace(/\t/g, '    ');
    js_summary = function () {
        var a, c = size.toString().split("").reverse(),
        d = rvalue.length.toString().split("").reverse(),
        b = c.length;
        for (a = 2; a < b; a += 3) {
            c[a] = "," + c[a];
        }
        b = d.length;
        for (a = 2; a < b; a += 3) {
            d[a] = "," + d[a];
        }
        c = c.reverse().join("");
        d = d.reverse().join("");
        if (c.charAt(0) === ",") {
            c = c.slice(1, c.length);
        }
        if (d.charAt(0) === ",") {
            d = d.slice(1, d.length);
        }
        return "<p><strong>Total input size:</strong> <em>" + c + "</em> characters</p><p><strong>Total output size:</strong> <em>" + d + "</em> characters</p>";
    };
    return rvalue;
};