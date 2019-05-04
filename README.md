开发
idea开发
右边的gradle窗口出现Tasks intellij runIde
如果没有内容点击刷新
点击runIde

编译
命令行下
gradle buildPlugin
build/distributions/translate-*.zip


这个项目没有项目管理很难受，现在开始这个
[项目开发入门](https://www.jetbrains.org/intellij/sdk/docs/tutorials/build_system/prerequisites.html)
[测试翻译的项目,idea插件开发文档](https://github.com/kana112233/intellij-sdk-docs.git)

idea 全文翻译插件 会标记哪些不用翻译。
选中翻译，可以翻译所有选中的内容，当翻译完会有提示。没有翻译完会一直卡着。
专门针对markdown文件、网页文本翻译，生成文件在当前文件夹，文件名称是
翻译文件名去除后缀加上_py.md
例如
test.md
变成
test_py.md

翻译实例请看
https://github.com/kana112233/tranlate-project.git

部署文件在.idea/下面

log
第一版: 翻译选中的文字
第二版: 选中文件夹翻译
-新建action不能使用。--删除项目里面的插件，重新启动。







