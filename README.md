# Build Types Gradle Plugin AppEngine extension

[Build Types Gradle Plugin](https://github.com/yj-abe/build-types-gradle-plugin)のAppEngine拡張。  
Build Types Gradle Pluginで定義したビルドタイプごとに、AppEngine Pluginのrun、deployの設定を行うことが出来ます。

実際の使い方などは[サンプル](https://github.com/yj-abe/build-types-sample/tree/master/gae)も参照してください。

## リモートリポジトリの追加

プラグインを使用するために、プロジェクトにmaven repositoryを追加する必要があります。  
以下のように、`build.gradle`にリポジトリを追加してください。

```groovy
buildscript {
    repositories {
        mavenCentral() // プラグインが使用する依存関係のために必要
        maven {
            url 'https://github.com/yj-abe/build-types-gradle-plugin/raw/gh-pages/repository'
        }
        maven {
            url 'https://github.com/yj-abe/build-types-appengine-gradle-plugin/raw/maven/repository'
        }
    }
    dependencies {
        classpath 'jp.cloudace:buildtypes:0.2.0'
        classpath 'jp.cloudace:buildtypes-appengine:0.1.3'
        classpath 'com.google.cloud.tools:appengine-gradle-plugin:2.2.0'
    }
}
```

## プラグインの適用

プラグインを使用する各プロジェクトの`build.gradle`に以下のコードスニペットを追加してください。

```groovy
apply plugin: 'com.google.cloud.tools.appengine'
apply plugin: 'jp.cloudace.buildtypes-appengine'
```

**※** このプラグインは本体のBuildTypesプラグインを自動的に有効化するため、上記に追加する必要はありません。

## DSL

```groovy
group 'jp.cloudace.myproject'

buildTypes {
    developOn "local" // 開発時に参照するビルドタイプの指定をします
    types {
        local { // 任意の名前をつけて、ビルドタイプを定義します
            debuggable = true // デバッグフラグの定義をします。デフォルトはfalseです。
            buildConfigField "String", "ENV_NAME", "\"ローカル環境\""
        }
        develop {
            buildConfigField "String", "ENV_NAME", "\"開発環境\""
        }
        staging {
            buildConfigField "String", "ENV_NAME", "\"ステージング環境\""
        }
        product {
            buildConfigField "String", "ENV_NAME", "\"本番環境\""
        }
    }
    appengine {
        deploy {
            develop {
                projectId = "INPUT YOUR DEVELOP SERVER PROJECT ID"
                version = "someone-test"
                promote = true
            }
            staging {
                projectId = "INPUT YOUR STAGING SERVER PROJECT ID"
            }
            product {
                projectId = "INPUT YOUR PRODUCT SERVER PROJECT ID"
            }
        }
        run {
            local {
                port = 8888
            }
        }
    }
}
```


## appengine extension

このプラグインを有効にすると、buildTypesに新たに`appengine` extensionが追加されます。  
このスクリプトブロックでは`deploy`、`run`スクリプトブロックを使って、各ビルドタイプ毎にローカルサーバーの起動、デプロイの設定を行うことが出来ます。

### deploy

appengineのデプロイに関する設定を行います。  
`deploy`ビルドスクリプトの中で、デプロイの設定を行いたいビルドタイプを指定し、各プロパティを設定します。

```groovy
appengine {
    deploy {
        dev {
            projectId = "dev-project-id"
            promote = true
        }
        stg {
            projectId = "stg-project-id"
            version = "fix-bug-check"
        }
        ...
    }
}
```


deployで指定出来るプロパティはAppEngine Pluginで定義されているものと同じになります。
各プロパティの詳細情報は[AppEngineのドキュメント](https://cloud.google.com/appengine/docs/standard/java/tools/gradle-reference#appenginedeploy)を参照してください。 
ただし、以下のものは当プラグインでデフォルト値を設定しています。

| プロパティ | デフォルト値 |
| :---: | :--- |
| projectId | GCLOUD_CONFIG |
| promote | false |
| version | GCLOUD_CONFIG |

各ビルドタイプ毎に、`appengine${BuildTypeName}Deploy`タスクが生成され、実行すると`deploy`で設定した内容で`appengineDeploy`タスクを実行します。

例:

```
./gradlew appengineDevDeploy
```

### run

appengineのローカルサーバー起動に関する設定を行います。  
`rc`ビルドスクリプトの中で、ローカルサーバーの設定を行いたいビルドタイプを指定し、各プロパティを設定します。

```groovy
appengine {
    run {
        local {
            port = 8888
        }
    }
}
```


runで指定出来るプロパティはAppEngine Pluginで定義されているものと同じになります。
各プロパティの詳細情報は[AppEngineのドキュメント](https://cloud.google.com/appengine/docs/standard/java/tools/gradle-reference#appenginerun)を参照してください。 

各ビルドタイプ毎に、`appengine${BuildTypeName}Run`タスクが生成され、実行すると`run`で設定した内容で`appengineRun`タスクを実行します。

例:

```
./gradlew appengineLocalRun
```
