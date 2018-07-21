# Toiletune

## このアプリについて
光を検知して音を流すAndroidアプリです。
トイレにアプリをインストールした端末を設置しアプリを起動しておき、明かりをつけるたびに音がなり、明かりを消すと音が止まります。

## 使い方
res/rawフォルダに音源ファイルを設置しビルドしてください。
音源ファイル名や再生確率は `presentation.viewmodel/MainActivityViewModel` 内の `createRepository()` を参照してください。