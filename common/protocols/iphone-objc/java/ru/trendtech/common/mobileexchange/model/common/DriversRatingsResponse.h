#import "RatingItem.h"
#import "NSMutableArray.h"
#import "NSMutableArray.h"

/**
 * Created by max on 06.02.14.
 */

@interface DriversRatingsResponse : NSObject {
  NSMutableArray * ratings;
}

@property(nonatomic, retain, readonly) NSMutableArray * ratings;
- (void) init;
@end
