# JavaScript Shell

![Icon](js-shell-icon.png)

An Android JavaScript sandbox app - for my school work.

## Highlighter

Highlights JavaScript code and inserts a cursor:

<table class='code-table'>
    <tr><td class='line-number'>1</td><td class='code-line'>&#xFEFF;<span class='code-highlight-keyword'>var</span> person <span class='code-highlight-operator'>=</span> <span class='code-highlight-operator'>{</span></tr>
    <tr><td class='line-number'>2</td><td class='code-line'>&#xFEFF;    name: <span class='code-highlight-string'>'Jim'</span><span class='code-highlight-operator'>,</span></tr>
    <tr><td class='line-number'>3</td><td class='code-line'>&#xFEFF;    age: <span class='code-highlight-number'>19</span><span class='code-highlight-operator'>,</span></tr>
    <tr><td class='line-number'>4</td><td class='code-line'>&#xFEFF;    height: <span class='code-highlight-number'>175</span><span class='code-highlight-operator'>,</span></tr>
    <tr><td class='line-number'>5</td><td class='code-line'>&#xFEFF;    eyeColor: <span class='code-highlight-string'>'blue'</span><span class='code-highlight-operator'>,</span></tr>
    <tr><td class='line-number'>6</td><td class='code-line'>&#xFEFF;    greet: <span class='code-highlight-keyword'>function</span> <span class='code-highlight-operator'>(</span><span class='code-highlight-operator'>)</span> <span class='code-highlight-operator'>{</span></tr>
    <tr><td class='line-number'>7</td><td class='code-line'>&#xFEFF;        <span class='code-highlight-keyword'>print</span><span class='code-highlight-operator'>(</span><span class='code-highlight-string'>'Hi, I am '</span> <span class='code-highlight-operator'>+</span> <span class='code-highlight-keyword'>this</span><span class='code-highlight-operator'>.</span>name<span class='code-highlight-operator'>)</span><span class='code-highlight-operator'>;</span></tr>
    <tr><td class='line-number'>8</td><td class='code-line'>&#xFEFF;    <span class='code-highlight-operator'>}</span></tr>
    <tr><td class='line-number'>9</td><td class='code-line'>&#xFEFF;<span class='code-highlight-operator'>}</span><span class='code-highlight-operator'>;</span></tr>
    <tr><td class='line-number'>10</td><td class='code-line'>&#xFEFF;</tr>
    <tr><td class='line-number'>11</td><td class='code-line'>&#xFEFF;<span class='code-highlight-comment'>/*</span></tr>
    <tr><td class='line-number'>12</td><td class='code-line'>&#xFEFF;<span class='code-highlight-comment'>Iterate over the object members:</span></tr>
    <tr><td class='line-number'>13</td><td class='code-line'>&#xFEFF;<span class='code-highlight-comment'>*/</span></tr>
    <tr><td class='line-number'>14</td><td class='code-line'>&#xFEFF;<span class='code-highlight-keyword'>print</span><span class='code-highlight-operator'>(</span><span class='code-highlight-string'>'Members:'</span><span class='code-highlight-operator'>)</span><span class='code-highlight-operator'>;</span></tr>
    <tr><td class='line-number'>15</td><td class='code-line'>&#xFEFF;<span class='code-highlight-keyword'>for</span><span class='code-highlight-operator'>(</span>member <span class='code-highlight-keyword'>in</span> person<span class='code-highlight-operator'>)</span> <span class='code-highlight-operator'>{</span></tr>
    <tr><td class='line-number'>16</td><td class='code-line'>&#xFEFF;    <span class='code-highlight-keyword'>print</span><span class='code-highlight-operator'>(</span><span class='code-highlight-string'>'- '</span> <span class='code-highlight-operator'>+</span> member<span class='code-highlight-operator'>)</span><span class='code-highlight-operator'>;</span></tr>
    <tr><td class='line-number'>17</td><td class='code-line'>&#xFEFF;<span class='code-highlight-operator'>}</span></tr>
    <tr><td class='line-number'>18</td><td class='code-line'>&#xFEFF;</tr>
    <tr class='active-line'><td class='line-number'>19</td><td class='code-line'>&#xFEFF;person<span class='code-highlight-operator'>.</span>greet<span class='code-highlight-operator'>(</span><span class='code-highlight-operator'>)</span><span class='code-highlight-operator'>;</span> <span class='code-highlight-comment'>// Call to a member function</span><span class='cursor-container'><span id='cursor'></span></span></tr>
</table>
