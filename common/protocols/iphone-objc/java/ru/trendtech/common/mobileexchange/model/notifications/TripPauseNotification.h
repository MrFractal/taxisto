#import "ItemLocation.h"

/**
 * Created by ivanenok on 4/14/2014.
 */

@interface TripPauseNotification : NSObject {
  long missionId;
  ItemLocation * location;
  BOOL begin;
}

@property(nonatomic) long missionId;
@property(nonatomic, retain) ItemLocation * location;
@property(nonatomic) BOOL begin;
- (void) init;
- (void) setMissionId:(long)missionId;
- (void) setLocation:(ItemLocation *)location;
- (void) setBegin:(BOOL)begin;
@end
