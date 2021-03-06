const path = require("path");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const CopyWebpackPlugin = require("copy-webpack-plugin");
const outputPath = "target/generated-resources/static/runtime";

const runtimeConfig = {
    mode: "development",
    target: "web",
    entry: {
        "scripts/theme": ["./src/startup.runtime.ts"],
    },
    output: {
        filename: "./[name].js",
        //path: path.resolve(__dirname, "dist/runtime"),
        path: path.resolve(__dirname, outputPath)
    },
    module: {
        rules: [
            {
                test: /\.scss$/,
                use: [
                    MiniCssExtractPlugin.loader,
                    { loader: "css-loader", options: { url: false } },
                    { loader: "postcss-loader" },
                    { loader: "sass-loader" }
                ]
            },
            {
                test: /\.tsx?$/,
                loader: "ts-loader",
                options: {
                    allowTsInNodeModules: true
                }
            },
            {
                test: /\.html$/,
                loader: "html-loader",
                options: {
                    esModule: true,
                    sources: false,
                    minimize: {
                        removeComments: false,
                        collapseWhitespace: false
                    }
                }
            },
            {
                test: /\.(svg)$/i,
                type: "asset/inline"
            },
            {
                test: /\.liquid$/,
                loader: "raw-loader"
            }
        ]
    },
    plugins: [
        new MiniCssExtractPlugin({ filename: "[name].css", chunkFilename: "[id].css" }),
        new CopyWebpackPlugin({
            patterns: [
                { from: `./src/themes/website/styles/fonts`, to: "styles/fonts" },
                { from: `./src/themes/website/assets` }
            ]
        })
    ],
    resolve: {
        extensions: [".js", ".ts", ".jsx", ".tsx", ".html", ".scss"],
        fallback: {
            "buffer": false,
            "stream": require.resolve("stream-browserify")
        }
    }
}

module.exports = runtimeConfig;