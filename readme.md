

|Name:|Long option:|Short Option:|Description:|
| :- | :- | :- | :- |
|Student File|–studentFile|-i|Location of student file|
|Output Directory|–outputDirectory|-o|Directory to output files. Optional: default is working directory|
|Year|–year|-y|The year of the current semester, in YYYY format. Optional: default is clock year|
|First Run|–firstRun|-f|<p>If this is the first year on this slack manager i.e. where all channels need to be created, or just Freshman/Sophomore</p><p>Optional: default is false</p>|

@Year param is used to determine graduation year, assuming code is run in the fall semester. I.e. if the year is set to 2022, senior placed students will be added to “2023” channels. This must be manually set if run during the Spring semester.

@First Run is used to determine whether all channels need to be created or just the Freshman year channel and Sophomore track channels
