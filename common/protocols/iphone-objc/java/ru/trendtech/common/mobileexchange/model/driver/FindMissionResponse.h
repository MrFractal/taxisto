#import "MissionInfo.h"

/**
 * Created by max on 06.02.14.
 */

@interface FindMissionResponse : NSObject {
  MissionInfo * missionInfo;
}

@property(nonatomic, retain) MissionInfo * missionInfo;
- (void) setMissionInfo:(MissionInfo *)missionInfo;
@end
