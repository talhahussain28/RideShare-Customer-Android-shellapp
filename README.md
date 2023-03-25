# android-Just her Rideshare-customer
This is a Just her Rideshare Customer Application Android README to show briefly about project.

Installation
Clone this repository and import into Android Studio

git clone https://github.com/himsmistry/andriod-just-her-rideshare-customer.git
Configuration
Keystores:
Create app/keystore.gradle with the following info:

- ext.key_alias='...'
- ext.key_password='...'
- ext.store_password='...'
And place both keystores under app/keystores/ directory:

playstore.keystore
stage.keystore
Build variants
Use the Android Studio Build Variants button to choose between production and staging flavors combined with debug and release build types

Generating signed APK
From Android Studio:

Build menu
Generate Signed APK...
Fill in the keystore information (you only need to do this once manually and then let Android Studio remember it)
Owners & Maintainers
This project is mantained by:

XongoLab Technologies LLP
Branch Naming Conventions
Maintaining a consistent branching model helps keep our working tree clean and organized. It also helps other developers and team members understand what is the intended purpose of this branch.

Below you will find some guidelines and tips on how to follow a correct branch naming convention.

Read A successful Git branching model by Vincent Driessen if you are unfamiliar with this branching model.

Driessen's model includes

A master branch, used only for release. Typical namemaster.
A "develop" branch off of that branch. That's the one used for most main-line work. Commonly named development.

Multiple feature branches off of the development branch. Name based on the name of the feature. These will be merged back into develop, not into the master or release branches.

Release branch to hold candidate releases, with only bug fixes and no new features. Typical name rc1.1.

Hotfixes are short-lived branches for changes that come from master and will go into master without development branch being involved.

Consider the following tips when choosing a branch name.

Group tokens - Use "grouping" tokens in front of your branch names. The groups can be named whatever you like to match your workflow. Here is a list of short well-defined tokens.

Token	
wip	Work in progress; changes that will not be finished soon.
ft	Feature branches. Branches that contain feature being newely developed.
bg	Bug fixes.
hf	Hot fixes. Those branches typically contain critical fixes to be directly merged into master.
exp	Experimental feature branches.
Use dashes to separate parts -- You may use most any delimiter you like in branch names, but I find dashes to be the most flexible. You might prefer to use slashes or dots. But slashes let you do some branch renaming when pushing or xlcabv1drivering to/from a remote.

ft-checkout-endpoints

bf-fix-homepage-item-spacing

exp-bootstrap-alpha-responsive-design

Do not use bare numbers -- Do not use use bare numbers (or hex numbers) as part of your branch naming scheme. It causes confusion and does not add any value.

Avoid long descriptive names -- Long branch names can be very helpful when you are looking at a list of branches. But it can get in the way when looking at decorated one-line logs as the branch names can eat up most of the single line and abbreviate the visible part of the log.

5.Important Branches -- Following branch are restricted to access

Branch	
master	Once project is finalized this branch is client's branch final source
develop	After shell application completed all daily development branch will be merged here
shell-app	Only Shell Application Branch
Used 3rd Parties Libraries
You can mentione all required libraries with reference link used in the app , as well as version number

implementation project(':ZDepthShadowLayout') 
implementation project(':ccp')
implementation 'com.crashlytics.sdk.android:crashlytics:2.9.9'


License
Copyright (C) 2017 XongoLab Technologies LLP 

Licensed under the XongoLab Technologies LLP, Version 1.0 (the "License");
Use of this source code strictly prohibiated outside of XongoLab Technologies LLP. 

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
