
/**
 * Created by max on 06.02.14.
 */

@interface FreeDriverResponse : NSObject {
  long missionId;
}

@property(nonatomic) long missionId;
- (void) setMissionId:(long)missionId;
@end
