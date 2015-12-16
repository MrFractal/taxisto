
/**
 * Created by max on 13.02.14.
 */

@interface CancelMissionRequest : NSObject {
  long initiatorId;
  long missionId;
  NSString * comment;
  int reason;
  BOOL force;
}

@property(nonatomic, retain) NSString * comment;
@property(nonatomic) long initiatorId;
@property(nonatomic) long missionId;
@property(nonatomic) int reason;
@property(nonatomic) BOOL force;
- (void) setComment:(NSString *)comment;
- (void) setInitiatorId:(long)initiatorId;
- (void) setMissionId:(long)missionId;
- (void) setReason:(int)reason;
- (void) setForce:(BOOL)force;
@end
